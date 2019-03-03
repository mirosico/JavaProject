import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

	private Dimension d;
	private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);

	private Image ii;
	private final Color dotColor = new Color(192, 192, 0);
	private Color mazeColor;
	private boolean isAlco = false;
	private boolean inGame = false;
	private boolean dying = false;
	private int yourPerson = 0;
	private int yourLevel = 1;
	private final int BLOCK_SIZE = 72;
	private final int N_BLOCKS = 9;
	private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;
	private final int PAC_ANIM_DELAY = 14;
	private final int personaj_ANIM_COUNT = 4;
	private final int MAX_Babochka = 1;
	private final int MAX_WATCH_WOMAN = 1;
	private int personaj_SPEED = 2;

	private int pacAnimCount = PAC_ANIM_DELAY;
	private int pacAnimDir = 1;
	private int personajAnimPos = 0;
	private int N_Babochka = 1;
	private int N_WATCH_WOMAN = 1;
	private int pacsLeft, score;
	private int[] dx, dy;
	private int[] Babochka_x, Babochka_y, Babochka_dx, Babochka_dy, Babochkapeed;
	private int[] watchWoman_x, watchWoman_y, watchWoman_dx, watchWoman_dy, watchWomanSpeed;

	private Image Babochka;
	private Image personaj1, personaj2, personaj2up, personaj2left, personaj2right, personaj2down, bab1;
	private Image personaj3up, personaj3down, personaj3left, personaj3right, fon;
	private Image personaj4up, personaj4down, personaj4left, personaj4right;

	private int personaj_x, personaj_y, personajd_x, personajd_y;
	private int req_dx, req_dy, view_dx, view_dy;

	private final int levelData3[] = { 19, 26, 26, 26, 26, 26, 26, 26, 6, 17, 24, 24, 24, 24, 24, 24, 24, 20, 17, 24,
			24, 24, 24, 24, 24, 24, 20, 17, 24, 24, 24, 24, 24, 24, 24, 20, 17, 24, 24, 24, 24, 24, 24, 24, 20, 17, 24,
			24, 24, 24, 24, 24, 24, 20, 17, 24, 24, 24, 24, 24, 24, 24, 20,

			17, 24, 24, 24, 24, 24, 24, 24, 20, 25, 24, 24, 24, 24, 24, 24, 24, 28 };

	private final int levelData1[] = { 19, 26, 10, 10, 10, 26, 10, 26, 6, 1, 8, 8, 8, 8, 8, 8, 24, 20, 1, 8, 8, 8, 8, 8,
			8, 8, 4, 1, 24, 8, 24, 24, 8, 24, 8, 20, 1, 8, 8, 8, 8, 8, 8, 8, 4, 17, 24, 24, 8, 24, 8, 8, 24, 20, 1, 8,
			8, 8, 8, 8, 8, 8, 4,

			17, 8, 24, 8, 24, 8, 8, 8, 20, 9, 24, 8, 8, 8, 8, 8, 8, 12 };

	private final int levelData2[] = { 19, 26, 10, 26, 10, 26, 10, 26, 6, 1, 8, 8, 8, 8, 8, 8, 24, 20, 17, 8, 24, 8, 24,
			24, 8, 8, 4, 1, 24, 8, 24, 24, 8, 24, 8, 20, 1, 8, 8, 24, 8, 8, 24, 8, 20, 17, 24, 24, 8, 24, 8, 8, 24, 20,
			17, 8, 8, 24, 8, 8, 24, 24, 4,

			17, 8, 24, 8, 24, 8, 8, 8, 20, 9, 24, 8, 8, 8, 8, 8, 24, 28 };

	private final int validSpeeds[] = { 3, 3, 3, 3, 3, 3 };
	private final int maxSpeed = 0;

	private int currentSpeed = 0;
	private int[] screenData;
	private Timer timer;

	public Board() {
		loadImages();
		initVariables();
		initBoard();
		JLabel background = new JLabel();
		add(background);
	}

	private void initBoard() {

		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.black);
		setDoubleBuffered(true);
	}

	private void initVariables() {

		screenData = new int[900];
		mazeColor = new Color(5, 100, 5);
		d = new Dimension(600, 600);
		Babochka_x = new int[MAX_Babochka];
		Babochka_dx = new int[MAX_Babochka];
		Babochka_y = new int[MAX_Babochka];
		Babochka_dy = new int[MAX_Babochka];
		Babochkapeed = new int[MAX_Babochka];
		watchWoman_x = new int[MAX_WATCH_WOMAN];
		watchWoman_dx = new int[MAX_WATCH_WOMAN];
		watchWoman_y = new int[MAX_WATCH_WOMAN];
		watchWoman_dy = new int[MAX_WATCH_WOMAN];
		watchWomanSpeed = new int[MAX_WATCH_WOMAN];
		dx = new int[4];
		dy = new int[4];

		timer = new Timer(40, this);
		timer.start();
	}

	@Override
	public void addNotify() {
		super.addNotify();

		initGame();
	}


	private void doAnim() {

		pacAnimCount--;

		if (pacAnimCount <= 0) {
			pacAnimCount = PAC_ANIM_DELAY;
			personajAnimPos = personajAnimPos + pacAnimDir;

			if (personajAnimPos == (personaj_ANIM_COUNT - 1) || personajAnimPos == 0) {
				pacAnimDir = -pacAnimDir;
			}
		}
	}

	private void playGame(Graphics2D g2d) {

		if (dying) {
			death();

		} else {
			paint(g2d);
			movepersonaj();
			drawpersonaj(g2d);
			checkMaze();
			moveBabochka(g2d);
			moveWatchWoman(g2d);
			checkMaze();
		}
	}

	private void drawWatchWoman(Graphics2D g2d, int x, int y) {

		g2d.drawImage(bab1, x, y, this);
	}

	private void moveWatchWoman(Graphics2D g2d) {

		int i;
		int pos;
		int count;

		for (i = 0; i < N_WATCH_WOMAN; i++) {
			if (watchWoman_x[i] % BLOCK_SIZE == 0 && watchWoman_y[i] % BLOCK_SIZE == 0) {
				pos = watchWoman_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (watchWoman_y[i] / BLOCK_SIZE);

				count = 0;

				if ((screenData[pos] & 1) == 0 && watchWoman_dx[i] != 1) {
					dx[count] = -1;
					dy[count] = 0;
					count++;
				}

				if ((screenData[pos] & 4) == 0 && watchWoman_dx[i] != -1) {
					dx[count] = 1;
					dy[count] = 0;
					count++;
				}

				if ((screenData[pos] & 8) == 0 && watchWoman_dy[i] != -1) {
					dx[count] = 0;
					dy[count] = 1;
					count++;
				}

				if (count == 0) {

					if ((screenData[pos] & 9) == 9) {
						watchWoman_dx[i] = 0;
						watchWoman_dy[i] = 0;
					} else {
						watchWoman_dx[i] = -watchWoman_dx[i];
						watchWoman_dy[i] = -watchWoman_dy[i];
					}

				} else {

					count = (int) (Math.random() * count);

					if (count > 3) {
						count = 3;
					}

					watchWoman_dx[i] = dx[count];
					watchWoman_dy[i] = dy[count];
				}

			}

			watchWoman_x[i] = watchWoman_x[i] + (watchWoman_dx[i] * watchWomanSpeed[i]);
			watchWoman_y[i] = watchWoman_y[i] + (watchWoman_dy[i] * watchWomanSpeed[i]);
			drawWatchWoman(g2d, watchWoman_x[i] + 1, watchWoman_y[i] + 13);

			if (personaj_x > (watchWoman_x[i] - 12) && personaj_x < (watchWoman_x[i] + 12)
					&& personaj_y > (watchWoman_y[i] - 12) && personaj_y < (watchWoman_y[i] + 12) && inGame) {

				dying = true;
			}
		}

	}

	
	private void showIntroScreen(Graphics2D g2d) {

		g2d.drawImage(new ImageIcon("pacpix/intro.png").getImage(), 0, 0, SCREEN_SIZE, SCREEN_SIZE + 100, this);
		g2d.drawImage(new ImageIcon("pacpix/persN.gif").getImage(), -40, 440, 300, 300, this);
		g2d.drawImage(new ImageIcon("pacpix/misha.gif").getImage(), 200, 460, 270, 270, this);
		g2d.drawImage(new ImageIcon("pacpix/egg.gif").getImage(), 400, 450, 270, 280, this);

		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(50, 350, SCREEN_SIZE - 100, 40);
		String s = "Натисніть клавішу 1, 2 або 3.";
		Font small = new Font("Helvetica", Font.BOLD, 18);
		g2d.setColor(Color.white);
		g2d.setFont(small);
		g2d.drawString(s, 200, 380);
		
	}


	private void drawScore(Graphics2D g) {

		int i;
		String s, lev, alco, isal;

		g.setFont(smallFont);
		g.setColor(new Color(96, 128, 255));
		s = "Кількість безалкогольних напоїв: " + score;
		g.drawString(s, SCREEN_SIZE / 2, SCREEN_SIZE + 16);

		g.setFont(smallFont);
		g.setColor(new Color(96, 128, 255));
		lev = "Рівень: " + yourLevel;
		g.drawString(lev, SCREEN_SIZE / 2, SCREEN_SIZE + 36);

		if (isAlco) {
			isal = "Високий!";
		} else
			isal = "Низький";

		g.setFont(smallFont);
		g.setColor(new Color(96, 128, 255));
		alco = "Рівень алкоголю в крові: " + isal;
		g.drawString(alco, SCREEN_SIZE / 2, SCREEN_SIZE + 56);

		for (i = 0; i < pacsLeft; i++) {
			g.drawImage(personaj3left, i * 28 + 8, SCREEN_SIZE + 1, this);
		}
	}

	private void checkMaze() {

		int i = 0;
		boolean finished = true;

		while (i < N_BLOCKS * N_BLOCKS && finished) {

			if ((screenData[i] & 48) != 0) {

				finished = false;
			}

			i++;
		}

		if (finished && personaj_x == 7 * BLOCK_SIZE && personaj_y <= 40) {
			if (yourLevel < 3) {
				yourLevel++;
			}
			score += 10;

			if (currentSpeed < maxSpeed) {
				currentSpeed++;
			}

			initLevel();
		}
	}

	private void death() {

		pacsLeft--;

		if (pacsLeft == 0) {
			inGame = false;
			yourLevel = 1;
		}

		continueLevel();
	}

	private void moveBabochka(Graphics2D g2d) {

		int i;
		int pos;
		int count;

		for (i = 0; i < N_Babochka; i++) {
			if (Babochka_x[i] % BLOCK_SIZE == 0 && Babochka_y[i] % BLOCK_SIZE == 0) {
				pos = Babochka_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (Babochka_y[i] / BLOCK_SIZE);

				count = 0;

				if ((screenData[pos] & 1) == 0 && Babochka_dx[i] != 1) {
					dx[count] = -1;
					dy[count] = 0;
					count++;
				}

				if ((screenData[pos] & 2) == 0 && Babochka_dy[i] != 1) {
					dx[count] = 0;
					dy[count] = -1;
					count++;
				}

				if ((screenData[pos] & 4) == 0 && Babochka_dx[i] != -1) {
					dx[count] = 1;
					dy[count] = 0;
					count++;
				}

				if ((screenData[pos] & 8) == 0 && Babochka_dy[i] != -1) {
					dx[count] = 0;
					dy[count] = 1;
					count++;
				}

				if (count == 0) {

					if ((screenData[pos] & 9) == 9) {
						Babochka_dx[i] = 0;
						Babochka_dy[i] = 0;
					} else {
						Babochka_dx[i] = -Babochka_dx[i];
						Babochka_dy[i] = -Babochka_dy[i];
					}

				} else {

					count = (int) (Math.random() * count);

					if (count > 3) {
						count = 3;
					}

					Babochka_dx[i] = dx[count];
					Babochka_dy[i] = dy[count];
				}

			}

			Babochka_x[i] = Babochka_x[i] + (Babochka_dx[i] * Babochkapeed[i]);
			Babochka_y[i] = Babochka_y[i] + (Babochka_dy[i] * Babochkapeed[i]);
			drawBabochka(g2d, Babochka_x[i] + 1, Babochka_y[i] + 1);

			if (personaj_x > (Babochka_x[i] - 12) && personaj_x < (Babochka_x[i] + 12) && personaj_y > (Babochka_y[i] - 12)
					&& personaj_y < (Babochka_y[i] + 12) && inGame) {

				dying = true;
			}
		}
	}

	private void drawBabochka(Graphics2D g2d, int x, int y) {
		g2d.drawImage(Babochka, x, y, this);
	}

	private void movepersonaj() {

		int pos;
		int ch;

		if (req_dx == -personajd_x && req_dy == -personajd_y) {
			personajd_x = req_dx;
			personajd_y = req_dy;
			view_dx = personajd_x;
			view_dy = personajd_y;
		}

		if (personaj_x % BLOCK_SIZE == 0 && personaj_y % BLOCK_SIZE == 0) {
			pos = personaj_x / BLOCK_SIZE + N_BLOCKS * (int) (personaj_y / BLOCK_SIZE);
			ch = screenData[pos];

			if ((ch & 16) != 0) {
				screenData[pos] = (int) (ch & 15);
				score++;
			}

			if (req_dx != 0 || req_dy != 0) {
				if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0) || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
						|| (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
						|| (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
					personajd_x = req_dx;
					personajd_y = req_dy;
					view_dx = personajd_x;
					view_dy = personajd_y;
				}
			}

			// Check for standstill
			if ((personajd_x == -1 && personajd_y == 0 && (ch & 1) != 0)
					|| (personajd_x == 1 && personajd_y == 0 && (ch & 4) != 0)
					|| (personajd_x == 0 && personajd_y == -1 && (ch & 2) != 0)

					|| (personajd_x == 0 && personajd_y == -1 && (ch & 1) == 0 && (ch & 4) == 0)

					|| (personajd_x == 0 && personajd_y == 1 && (ch & 8) != 0)) {
				personajd_x = 0;
				personajd_y = 0;
			}

		}
		personaj_x = personaj_x + personaj_SPEED * personajd_x;
		personaj_y = personaj_y + personaj_SPEED * personajd_y;
	}

	private void drawpersonaj(Graphics2D g2d) {

		if (view_dx == -1) {
			drawPacnanLeft(g2d);
		} else if (view_dx == 1) {
			drawpersonajRight(g2d);
		} else if (view_dy == -1) {
			drawpersonajUp(g2d);
		} else {
			drawpersonajDown(g2d);
		}
	}

	private void drawpersonajUp(Graphics2D g2d) {

		switch (personajAnimPos) {
		case 1:
			g2d.drawImage(personaj2up, personaj_x + 1, personaj_y - 1, this);
			break;
		case 2:
			g2d.drawImage(personaj3up, personaj_x + 1, personaj_y - 4, this);
			break;
		case 3:
			g2d.drawImage(personaj4up, personaj_x + 1, personaj_y - 4, this);
			break;
		default:
			g2d.drawImage(personaj1, personaj_x + 1, personaj_y - 4, this);
			break;
		}
	}

	private void drawpersonajDown(Graphics2D g2d) {

		switch (personajAnimPos) {
		case 1:
			g2d.drawImage(personaj2down, personaj_x + 1, personaj_y - 4, this);
			break;
		case 2:
			g2d.drawImage(personaj3down, personaj_x + 1, personaj_y - 4, this);
			break;
		case 3:
			g2d.drawImage(personaj4down, personaj_x + 1, personaj_y - 4, this);
			break;
		default:
			g2d.drawImage(personaj2, personaj_x + 1, personaj_y - 4, this);
			break;
		}
	}

	private void drawPacnanLeft(Graphics2D g2d) {

		switch (personajAnimPos) {
		case 1:
			g2d.drawImage(personaj2left, personaj_x + 1, personaj_y - 4, this);
			break;
		case 2:
			g2d.drawImage(personaj3left, personaj_x + 1, personaj_y - 4, this);
			break;
		case 3:
			g2d.drawImage(personaj4left, personaj_x + 1, personaj_y - 4, this);
			break;
		default:
			g2d.drawImage(personaj2, personaj_x + 1, personaj_y - 4, this);
			break;
		}
	}

	public void paint(Graphics2D gt) {
		gt.drawImage(fon, getWidth(), getHeight(), this);
	}

	private void drawpersonajRight(Graphics2D g2d) {

		switch (personajAnimPos) {
		case 1:
			g2d.drawImage(personaj2right, personaj_x + 1, personaj_y - 4, this);
			break;
		case 2:
			g2d.drawImage(personaj3right, personaj_x + 1, personaj_y - 4, this);
			break;
		case 3:
			g2d.drawImage(personaj4right, personaj_x + 1, personaj_y - 4, this);
			break;
		default:
			g2d.drawImage(personaj1, personaj_x + 1, personaj_y - 4, this);
			break;
		}
	}

	private void drawMaze(Graphics2D g2d) {

		int i = 0;
		int x, y;
		int bottle = 0;
		for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
			for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
				bottle++;
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(2));

				if ((screenData[i] & 1) != 0) {
					g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
				}

				if ((screenData[i] & 2) != 0) {
					g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
				}

				if ((screenData[i] & 4) != 0) {
					g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
				}

				if ((screenData[i] & 8) != 0) {
					g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
				}

				if ((screenData[i] & 16) != 0) {
					if (bottle % 2 == 0)
						g2d.drawImage(new ImageIcon("pacpix/but1.png").getImage(), x + 11, y + 20, 30, 30, this);
					if (bottle % 3 == 0)
						g2d.drawImage(new ImageIcon("pacpix/but4.png").getImage(), x + 11, y + 20, 30, 30, this);
					if (bottle % 2 != 0)
						g2d.drawImage(new ImageIcon("pacpix/but3.png").getImage(), x + 11, y + 20, 30, 30, this);
					if (bottle == 7 || bottle == 5)
						g2d.drawImage(new ImageIcon("pacpix/but2.png").getImage(), x + 11, y + 20, 30, 30, this);
				}

				i++;
			}
		}
	}

	private void initGame() {
		if (yourPerson == 3)
			personaj_SPEED = 3;
		pacsLeft = 3;
		score = 0;
		initLevel();
		N_Babochka = 1;
		currentSpeed = 0;
	}

	private void initLevel() {

		int i;

		if (yourLevel == 1)
			for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
				screenData[i] = levelData1[i];
			}
		if (yourLevel == 2)
			for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
				screenData[i] = levelData2[i];
			}
		if (yourLevel == 3)
			for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
				screenData[i] = levelData3[i];
			}

		continueLevel();
	}

	private void continueLevel() {

		int i;
		int dx = 1;
		int random;

		for (i = 0; i < N_Babochka; i++) {

			watchWoman_y[i] = 8 * BLOCK_SIZE;
			watchWoman_x[i] = 4 * BLOCK_SIZE;
			watchWoman_dy[i] = 0;
			watchWoman_dx[i] = dx;
			Babochka_y[i] = 8 * BLOCK_SIZE;
			Babochka_x[i] = 8 * BLOCK_SIZE;
			Babochka_dy[i] = 0;
			Babochka_dx[i] = dx;
			dx = -dx;
			random = (int) (Math.random() * (currentSpeed + 1));

			if (random > currentSpeed) {
				random = currentSpeed;
			}

			Babochkapeed[i] = validSpeeds[random];
			watchWomanSpeed[i] = 1;
		}

		personaj_x = 4 * BLOCK_SIZE;
		personaj_y = 7 * BLOCK_SIZE;
		personajd_x = 0;
		personajd_y = 0;
		req_dx = 0;
		req_dy = 0;
		view_dx = 0;
		view_dy = -1;
		dying = false;
	}

	private void loadImages() {
		bab1 = new ImageIcon("pacpix/bab1.gif").getImage();
		Babochka = new ImageIcon("pacpix/fly1.png").getImage();
		if (yourPerson == 1) {
			personaj1 = new ImageIcon("pacpix/pers11.png").getImage();
			personaj2 = new ImageIcon("pacpix/persLeft1.png").getImage();
			personaj2up = new ImageIcon("pacpix/pers11.png").getImage();
			personaj3up = new ImageIcon("pacpix/pers22.png").getImage();
			personaj4up = new ImageIcon("pacpix/pers33.png").getImage();

			personaj2down = new ImageIcon("pacpix/persLeft3.png").getImage();
			personaj3down = new ImageIcon("pacpix/persLeft2.png").getImage();
			personaj4down = new ImageIcon("pacpix/persLeft1.png").getImage();

			personaj2left = new ImageIcon("pacpix/persLeft3.png").getImage();
			personaj3left = new ImageIcon("pacpix/persLeft2.png").getImage();
			personaj4left = new ImageIcon("pacpix/persLeft1.png").getImage();

			personaj2right = new ImageIcon("pacpix/pers11.png").getImage();
			personaj3right = new ImageIcon("pacpix/pers22.png").getImage();
			personaj4right = new ImageIcon("pacpix/pers33.png").getImage();
		} else if (yourPerson == 2) {
			personaj1 = new ImageIcon("pacpix/misha1.png").getImage();
			personaj2 = new ImageIcon("pacpix/mishaLeft1.png").getImage();
			personaj2up = new ImageIcon("pacpix/misha1.png").getImage();
			personaj3up = new ImageIcon("pacpix/misha2.png").getImage();
			personaj4up = new ImageIcon("pacpix/misha3.png").getImage();

			personaj2down = new ImageIcon("pacpix/mishaLeft3.png").getImage();
			personaj3down = new ImageIcon("pacpix/mishaLeft2.png").getImage();
			personaj4down = new ImageIcon("pacpix/mishaLeft1.png").getImage();

			personaj2left = new ImageIcon("pacpix/mishaLeft3.png").getImage();
			personaj3left = new ImageIcon("pacpix/mishaLeft2.png").getImage();
			personaj4left = new ImageIcon("pacpix/mishaLeft1.png").getImage();

			personaj2right = new ImageIcon("pacpix/misha1.png").getImage();
			personaj3right = new ImageIcon("pacpix/misha2.png").getImage();
			personaj4right = new ImageIcon("pacpix/misha3.png").getImage();
		} else if (yourPerson == 3) {
			personaj1 = new ImageIcon("pacpix/Egg1.png").getImage();
			personaj2 = new ImageIcon("pacpix/eggLeft1.png").getImage();
			personaj2up = new ImageIcon("pacpix/Egg1.png").getImage();
			personaj3up = new ImageIcon("pacpix/egg2.png").getImage();
			personaj4up = new ImageIcon("pacpix/egg3.png").getImage();

			personaj2down = new ImageIcon("pacpix/eggLeft3.png").getImage();
			personaj3down = new ImageIcon("pacpix/eggLeft2.png").getImage();
			personaj4down = new ImageIcon("pacpix/eggLeft1.png").getImage();

			personaj2left = new ImageIcon("pacpix/eggLeft3.png").getImage();
			personaj3left = new ImageIcon("pacpix/eggLeft2.png").getImage();
			personaj4left = new ImageIcon("pacpix/eggLeft1.png").getImage();

			personaj2right = new ImageIcon("pacpix/egg1.png").getImage();
			personaj3right = new ImageIcon("pacpix/egg2.png").getImage();
			personaj4right = new ImageIcon("pacpix/egg3.png").getImage();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	private void doDrawing(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(new ImageIcon("pacpix/background.png").getImage(), 0, 0, SCREEN_SIZE, SCREEN_SIZE, this);
		g2d.setColor(Color.black);
		drawMaze(g2d);
		drawScore(g2d);
		doAnim();

		if (inGame) {
			playGame(g2d);
		} else {
			showIntroScreen(g2d);
		}
		g2d.drawImage(ii, 5, 5, this);
		Toolkit.getDefaultToolkit().sync();
		g2d.dispose();
	}

	class TAdapter extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {

			int key = e.getKeyCode();
			if (inGame) {
				if (score > 70 && yourPerson != 1) {
					isAlco = true;
					if (key == KeyEvent.VK_RIGHT) {
						req_dx = -1;
						req_dy = 0;
					} else if (key == KeyEvent.VK_LEFT) {
						req_dx = 1;
						req_dy = 0;
					} else if (key == KeyEvent.VK_DOWN) {
						req_dx = 0;
						req_dy = -1;
					} else if (key == KeyEvent.VK_UP) {
						req_dx = 0;
						req_dy = 1;
					} else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
						inGame = false;
					} else if (key == KeyEvent.VK_PAUSE) {
						if (timer.isRunning()) {
							timer.stop();
						} else {
							timer.start();
						}
					}
				} else {
					if (key == KeyEvent.VK_LEFT) {
						req_dx = -1;
						req_dy = 0;
					} else if (key == KeyEvent.VK_RIGHT) {
						req_dx = 1;
						req_dy = 0;
					} else if (key == KeyEvent.VK_UP) {
						req_dx = 0;
						req_dy = -1;
					} else if (key == KeyEvent.VK_DOWN) {
						req_dx = 0;
						req_dy = 1;
					} else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
						inGame = false;
					} else if (key == KeyEvent.VK_PAUSE) {
						if (timer.isRunning()) {
							timer.stop();
						} else {
							timer.start();
						}
					}
				}
			} else {
				if (key == '1') {
					yourPerson = 1;
					inGame = true;
					initGame();
					yourPerson = 1;
					loadImages();
					personaj_SPEED = 2;
				}
				if (key == '2') {
					yourPerson = 2;
					inGame = true;
					initGame();
					yourPerson = 2;
					loadImages();
					pacsLeft = 4;
					personaj_SPEED = 2;

				}
				if (key == '3') {
					yourPerson = 3;
					inGame = true;
					initGame();
					yourPerson = 3;
					loadImages();

				}
					
				

			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

			int key = e.getKeyCode();

			if (key == Event.LEFT || key == Event.RIGHT || key == Event.UP || key == Event.DOWN) {
				req_dx = 0;
				req_dy = 0;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		repaint();
	}
}