
package com.cli;

import com.sessions.Session;
import com.sessions.SessionService;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class CLI {

  private final SessionService service;
  private final Scanner scanner;

  private boolean running;
  private Session activeSession;
  private LocalDateTime sessionStartedAt;

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
    System.out.println(title);

    printManual();

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

            System.out.println("Session #" + activeSession.getSessionId() + " started.");

          } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
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

            System.out.println("Session #" + endedSession.getSessionId() + " ended.");

            if (activeSession != null && activeSession.getSessionId().equals(id)) {

              activeSession = null;
              sessionStartedAt = null;
            }

          } catch (NumberFormatException e) {
            System.out.println("Invalid session ID.");

          } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
          }
        }

        case "status" -> {
          if (activeSession == null) {
            System.out.println("No active session.");
          } else {
            Duration elapsed = Duration.between(sessionStartedAt, LocalDateTime.now());

            System.out.printf(
                "Session #%d | %02d:%02d:%02d%n",
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

        default -> System.out.println("Unknown command. Type 'help' for available commands.");
      }
    }

    scanner.close();
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
