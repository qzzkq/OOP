package ru.nsu.kiryushin;

import java.util.Scanner;

/**
 * Console Blackjack game.
 */
public class Game {
    private int winsPlayer = 0;
    private int winsDealer = 0;

    /**
     * Starts the interactive game loop.
     */
    public void game() {
        System.out.println(
                "Добро пожаловать в Блэкджек!\nУкажи количество колод!\n");
        Scanner sc = new Scanner(System.in);

        int numDeck = readNumDecks(sc);
        int totalCards = 52 * numDeck;
        int round = 1;

        Deck deck = Deck.create(numDeck);
        while (true) {
            deck = ensureDeckHasCards(numDeck, totalCards, deck);

            if (!waitEnterOrQuit(
                    sc,
                    "Нажмите Enter, чтобы начать раунд, или 'q', чтобы завершить игру...")) {
                System.out.println(
                        "Игра завершена. Итоговый счёт: " + winsPlayer + ":" + winsDealer);
                sc.close();
                return;
            }

            System.out.println("Раунд " + round + "\n");

            int result = playRound(sc, deck);
            switch (result) {
                case 1:
                    System.out.println("Вы победили!\n");
                    winsPlayer++;
                    break;
                case 0:
                    System.out.println("Ничья!\n");
                    break;
                case -1:
                    System.out.println("Вы проиграли!\n");
                    winsDealer++;
                    break;
                default:
                    throw new IllegalStateException("Неожиданный результат раунда: " + result);
            }

            System.out.println(scoreLine());
            round++;
        }
    }

    /**
     * Ensures that the current deck still has enough cards for the next round.
     *
     * @param numDeck number of decks chosen for the game
     * @param totalCards total cards available at the beginning of the game
     * @param deck current deck instance
     * @return existing deck if it still has cards, otherwise a reshuffled deck
     */
    private Deck ensureDeckHasCards(int numDeck, int totalCards, Deck deck) {
        if (numDeck == 1) {
            if (deck.size() < 22) {
                return Deck.create(numDeck);
            }
        } else {
            int cutRemaining = (int) Math.round(totalCards * 0.25);
            if (deck.size() <= cutRemaining) {
                return Deck.create(numDeck);
            }
        }
        return deck;
    }

    /**
     * Reads from the input the number of decks to use.
     *
     * @param sc scanner connected to the input stream
     * @return number of decks entered by the user
     */
    private int readNumDecks(Scanner sc) {
        while (true) {
            if (sc.hasNextInt()) {
                int numDeck = sc.nextInt();
                sc.nextLine();
                String suffix = (numDeck == 1) ? " колодой!\n" : " колодами!\n";
                System.out.println(
                        "\nИгра будет с " + numDeck + suffix + "\nНачинаем игру...\n");
                return numDeck;
            }
            System.out.println("Это не целое число. Попробуйте ещё раз:");
            sc.nextLine();
        }
    }

    /**
     * Builds the current score line for display to the player.
     *
     * @return formatted score string
     */
    private String scoreLine() {
        String whoLeader;
        if (winsPlayer > winsDealer) {
            whoLeader = " в вашу пользу";
        } else if (winsPlayer < winsDealer) {
            whoLeader = " в пользу дилера";
        } else {
            whoLeader = " - ничья";
        }
        return "Счёт " + winsPlayer + ":" + winsDealer + whoLeader + "\n";
    }

    /**
     * Plays a single round.
     *
     * @param sc scanner for user input
     * @param deck active deck to draw cards from
     * @return 1 if player wins, 0 if push, -1 if dealer wins
     */
    private int playRound(Scanner sc, Deck deck) {
        int sumDealer;
        int sumPlayer;
        Player player = new Player(deck.getCard(), deck.getCard());
        Dealer dealer = new Dealer(deck.getCard(), deck.getCard());
        System.out.println("Дилер раздал карты");

        sumPlayer = player.getSumHand();
        sumDealer = dealer.getSumHand();

        if (sumDealer == sumPlayer && sumDealer == 21) {
            dealer.playerFinishedTurn();
            System.out.println(showHands(dealer, player));
            System.out.println("Сумма карт у вас и у дилера равна 21. Блэкджек.");
            return 0;
        } else if (sumDealer == 21) {
            dealer.playerFinishedTurn();
            System.out.println(showHands(dealer, player));
            System.out.println("Сумма карт дилера равна 21. Блэкджек.");
            return -1;
        } else if (sumPlayer == 21) {
            dealer.playerFinishedTurn();
            System.out.println(showHands(dealer, player));
            System.out.println("Сумма ваших карт равна 21. Блэкджек.");
            return 1;
        }

        System.out.println(showHands(dealer, player));
        System.out.println("Ваш ход\n-------");

        while (true) {
            System.out.println("Введите '1', чтобы взять карту, и '0', чтобы остановиться...");
            int ans = read01(sc);

            if (ans == 0) {
                break;
            }

            Card card = deck.getCard();
            System.out.println("Вы открыли карту " + card.getCardName() + "\n");
            player.addCard(card);
            System.out.println(showHands(dealer, player));
            sumPlayer = player.getSumHand();
            if (sumPlayer > 21) {
                System.out.println("Сумма карт больше 21.");
                return -1;
            } else if (sumPlayer == 21) {
                System.out.println("Сумма карт равна 21. Ход переходит дилеру.\n");
                break;
            }
        }
        System.out.println("Ход дилера\n----------");
        waitEnter(sc, "Нажмите Enter, чтобы дилер открыл закрытую карту...");
        dealer.playerFinishedTurn();
        System.out.println(
                "Дилер открывает закрытую карту " + dealer.getClosedCard().getCardName() + "\n");
        System.out.println(showHands(dealer, player));
        sumDealer = dealer.getSumHand();
        while (sumDealer < 17) {
            waitEnter(sc, "Нажмите Enter, чтобы дилер взял следующую карту...");
            Card card = deck.getCard();
            System.out.println("Дилер открывает карту " + card.getCardName());
            dealer.addCard(card);
            System.out.println(showHands(dealer, player));
            sumDealer = dealer.getSumHand();
        }
        if (sumDealer > 21) {
            System.out.println("У дилера перебор.");
            return 1;
        } else if (sumDealer == sumPlayer) {
            System.out.println("Очки равны.");
            return 0;
        } else if (sumDealer > sumPlayer) {
            System.out.println("Сумма очков дилера больше, чем ваша.");
            return -1;
        } else {
            System.out.println("Сумма ваших очков больше, чем у дилера.");
            return 1;
        }
    }

    /**
     * Builds a multi-line view of both hands for display.
     *
     * @param dealer dealer hand
     * @param player player hand
     * @return formatted string with both hands
     */
    private String showHands(Dealer dealer, Player player) {
        return "    "
                + "Ваши карты: "
                + player.getStringHandPlayer()
                + "\n    Карты дилера: "
                + dealer.getHandString()
                + "\n";
    }

    /**
     * Reads a user choice: 0 or 1.
     *
     * @param sc scanner
     * @return 0 or 1
     */
    private int read01(Scanner sc) {
        while (true) {
            if (sc.hasNextInt()) {
                int x = sc.nextInt();
                sc.nextLine();
                if (x == 0 || x == 1) {
                    return x;
                }
            } else {
                sc.nextLine();
            }
            System.out.println("Введите 0 или 1:");
        }
    }

    /**
     * Message for starting a round: Enter to continue, 'q' to quit.
     *
     * @param sc scanner
     * @param message text to display
     * @return true to continue; false to quit
     */
    private boolean waitEnterOrQuit(Scanner sc, String message) {
        while (true) {
            System.out.println(message);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                return true;
            } else if (line.equalsIgnoreCase("q")) {
                return false;
            } else {
                System.out.println(
                        "Введите только Enter для продолжения или 'q' для выхода.");
            }
        }
    }

    /**
     * Simple “press Enter to continue” pause.
     *
     * @param sc scanner
     * @param message message to display
     */
    private void waitEnter(Scanner sc, String message) {
        while (true) {
            System.out.println(message);
            String line = sc.nextLine().trim();
            if (line.isEmpty()) {
                return;
            }
        }
    }
}
