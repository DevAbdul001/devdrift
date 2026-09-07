package com.cli;

import com.sessions.Session;
import com.sessions.SessionService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CLI {

  // ANSI Color Escape Codes
  private static final String RESET = "\u001B[0m";
  private static final String BOLD = "\u001B[1m";
  private static final String CYAN = "\u001B[36m";
  private static final String GREEN = "\u001B[32m";
  private static final String RED = "\u001B[31m";

  private final SessionService service;
  private final Scanner scanner;

  private boolean running;
  private Session activeSession;
  private LocalDateTime sessionStartedAt;

  private ScheduledExecutorService timerExecutor;

  public CLI(SessionService service) {
    this.service = service;
    this.scanner = new Scanner(System.in);
  }

  public void run() {
    String title =
        """
	██████╗ ███████╗██╗   ██╗██████╗ ██████╗ ██╗███████╗████████╗
	██╔══██╗██╔════╝██║   ██║██╔══██╗██╔══██╗██║██╔════╝╚══██╔══╝
	██║  ██║█████╗  ██║   ██║██║  ██║██████╔╝██║█████╗     ██║
	██║  ██║██╔══╝  ╚██╗ ██╔╝██║  ██║██╔══██╗██║██╔══╝     ██║
	██████╔╝███████╗ ╚████╔╝ ██████╔╝██║  ██║██║██║        ██║
	╚═════╝ ╚══════╝  ╚═══╝  ╚═════╝ ╚═╝  ╚═╝╚═╝╚═╝        ╚═╝

		""";
    System.out.println(BOLD + title + RESET);

    printManual();
    startLiveTimerThread();

    running = true;

    while (running) {
      System.out.print("\nDevDrift > ");

      String input = scanner.nextLine().trim();

      if (input.isEmpty()) {
        continue;
      }

      String[] parts = input.split("\\s+", 2);
      String command = parts[0].toLowerCase();

      switch (command) {
        case "start" -> {
          String notes = parts.length > 1 ? parts[1] : null;
          try {
            activeSession = service.start(notes);
            sessionStartedAt = activeSession.getStartedAt();
            System.out.println(
                GREEN + "Session #" + activeSession.getSessionId() + " started." + RESET);
          } catch (Exception e) {
            System.out.println(RED + "Error: " + e.getMessage() + RESET);
          }
        }

        case "end" -> {
          if (parts.length < 2) {
            System.out.println("Usage: end <session-id>");
            continue;
          }
          try {
            Long id = Long.parseLong(parts[1]);
            Session endedSession = service.end(id);
            System.out.println(
                CYAN + "Session #" + endedSession.getSessionId() + " ended." + RESET);

            if (activeSession != null && activeSession.getSessionId().equals(id)) {
              activeSession = null;
              sessionStartedAt = null;
              clearStatusLine();
            }
          } catch (NumberFormatException e) {
            System.out.println(RED + "Invalid session ID." + RESET);
          } catch (Exception e) {
            System.out.println(RED + "Error: " + e.getMessage() + RESET);
          }
        }

        case "status" -> {
          if (activeSession == null) {
            System.out.println("No active session.");
          } else {
            Duration elapsed = Duration.between(sessionStartedAt, LocalDateTime.now());
            System.out.printf(
                CYAN + "Session #%d" + RESET + " | " + GREEN + "%02d:%02d:%02d%n" + RESET,
                activeSession.getSessionId(),
                elapsed.toHours(),
                elapsed.toMinutesPart(),
                elapsed.toSecondsPart());
          }
        }

        case "help" -> printManual();

        case "exit", "quit" -> {
          running = false;
          System.out.println("Goodbye.");
        }

        default ->
            System.out.println(
                RED + "Unknown command. Type 'help' for available commands." + RESET);
      }
    }

    if (timerExecutor != null) {
      timerExecutor.shutdownNow();
    }
    scanner.close();
  }

  /**
   * Spawns a background thread that refreshes a dynamic status line directly under the prompt row.
   */
  private void startLiveTimerThread() {
    timerExecutor =
        Executors.newSingleThreadScheduledExecutor(
            runnable -> {
              Thread thread = new Thread(runnable);
              thread.setDaemon(true);
              return thread;
            });

    timerExecutor.scheduleAtFixedRate(
        () -> {
          if (activeSession != null && sessionStartedAt != null) {
            Duration elapsed = Duration.between(sessionStartedAt, LocalDateTime.now());

            String timerString =
                String.format(
                    " %s[ LIVE STATUS ]%s Session: %s#%d%s | Elapsed: %s%02d:%02d:%02d%s",
                    BOLD,
                    RESET,
                    CYAN,
                    activeSession.getSessionId(),
                    RESET,
                    GREEN,
                    elapsed.toHours(),
                    elapsed.toMinutesPart(),
                    elapsed.toSecondsPart(),
                    RESET);

            // Save position, move down one row, wipe line, output colored timer, bounce back up
            System.out.print("\u001B[s\n\u001B[K" + timerString + "\u001B[u");
            System.out.flush();
          }
        },
        0,
        1,
        TimeUnit.SECONDS);
  }

  /** Drops down one line to clear the live timer block when a session wraps up. */
  private void clearStatusLine() {
    System.out.print("\u001B[s\n\u001B[K\u001B[u");
    System.out.flush();
  }

  private void printManual() {
    System.out.println(
        """

                Commands:

                  start [notes]       Start a coding session
                  end <session-id>   End a coding session
                  status              Show the current session timer
                  help                Show this manual
                  exit                Exit DevDrift

                """);
  }
}
