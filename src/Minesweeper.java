import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;

public class Minesweeper extends JFrame {
    private final JButton[][] buttons;
    private final boolean[][] mines;
    private final int[][] surroundingMines;
    private int uncoveredCells;

    public Minesweeper() {
        setTitle("Minesweeper");//Установка заголовка окна
        setDefaultCloseOperation(EXIT_ON_CLOSE);//Настройки закрытия окна устанавливаются на "EXITONCLOSE".
        setLayout(new GridLayout(10, 10));//устанавливает менеджер компоновки сеткой с размерами 10х10

        buttons = new JButton[10][10];//создает массив кнопок размером 10х10.
        mines = new boolean[10][10];//создает массив boolean для хранения информации о минах на поле 10х10
        surroundingMines = new int[10][10];//создает массив int для хранения информации о количестве окружающих мин для каждой ячейки
        uncoveredCells = 0;//количество открытых ячеек

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                buttons[i][j] = new JButton();//создание новую кнопки и сохранение ее в массив кнопок по координатам i и j.
                buttons[i][j].addActionListener(new CellClickListener(i, j));//добавление слушателя для реакции на нажатие кнопки
                add(buttons[i][j]);
            }
        }

        placeMines();//вызов метода случайной генерации мин
        countSurroundingMines();//вызов метода для подсчёта окружающих мин

        pack();//установка оптимального размера окна
        setVisible(true);//установка видимости окна
    }

    private void placeMines() {//метод генерации мин
        Random random = new Random();
        int placedMines = 0;
        while (placedMines < 10) {
            int i = random.nextInt(10);
            int j = random.nextInt(10);
            if (!mines[i][j]) {
                mines[i][j] = true;
                placedMines++;
            }
        }
    }

    private void countSurroundingMines() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!mines[i][j]) {
                    int count = 0;
                    if (i > 0 && mines[i - 1][j]) count++;
                    if (i < 9 && mines[i + 1][j]) count++;
                    if (j > 0 && mines[i][j - 1]) count++;
                    if (j < 9 && mines[i][j + 1]) count++;
                    if (i > 0 && j > 0 && mines[i - 1][j - 1]) count++;
                    if (i < 9 && j < 9 && mines[i + 1][j + 1]) count++;
                    if (i > 0 && j < 9 && mines[i - 1][j + 1]) count++;
                    if (i < 9 && j > 0 && mines[i + 1][j - 1]) count++;
                    surroundingMines[i][j] = count;
                }
            }
        }
    }

    private void uncoverCell(int i, int j) {
        if (mines[i][j]) {//проверка условия, если нажата мина, то поражение
            loseGame();
        } else {//если мина не нажата, проверяет есть ли мины рядом
            buttons[i][j].setText(Integer.toString(surroundingMines[i][j]));
            buttons[i][j].setEnabled(false);
            uncoveredCells++;
            if (uncoveredCells == 90) {
                winGame();
            }
            if (surroundingMines[i][j] == 0) {
                uncoverSurroundingCells(i, j);
            }
        }
    }

    private void uncoverSurroundingCells(int i, int j) {
        if (i > 0 && buttons[i - 1][j].isEnabled()) uncoverCell(i - 1, j);
        if (i < 9 && buttons[i + 1][j].isEnabled()) uncoverCell(i + 1, j);
        if (j > 0 && buttons[i][j - 1].isEnabled()) uncoverCell(i, j - 1);
        if (j < 9 && buttons[i][j + 1].isEnabled()) uncoverCell(i, j + 1);
        if (i > 0 && j > 0 && buttons[i - 1][j - 1].isEnabled()) uncoverCell(
                i - 1,
                j - 1
        );//если рядом с нажатой кнопкой нет мин, раскрывает сверху, снизу,слева,справа
        if (i < 9 && j < 9 && buttons[i + 1][j + 1].isEnabled()) uncoverCell(
                i + 1,
                j + 1
        );
        if (i > 0 && j < 9 && buttons[i - 1][j + 1].isEnabled()) uncoverCell(
                i - 1,
                j + 1
        );
        if (i < 9 && j > 0 && buttons[i + 1][j - 1].isEnabled()) uncoverCell(
                i + 1,
                j - 1
        );
    }

    private void winGame() {
        JOptionPane.showMessageDialog(this, "You won!");
        System.exit(0);
    }

    private void loseGame() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (mines[i][j]) {
                    ImageIcon icon = new ImageIcon("C:\\Users\\karim\\IdeaProjects\\minesweeper\\src\\1.jpg"); // Путь к изображению
                    buttons[i][j].setIcon(icon);
                }
                buttons[i][j].setEnabled(false);
            }
        }
        JOptionPane.showMessageDialog(this, "Ты проиграл и на твой компьютер установлен майнер.");
        System.exit(0);
    }


    private class CellClickListener implements ActionListener {
        private final int i;
        private final int j;

        public CellClickListener(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public void actionPerformed(ActionEvent e) {
            uncoverCell(i, j);
        }
    }
}