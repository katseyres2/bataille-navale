package grid;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import boat.Boat;

/**
 * 
 */
public class Grid {
	static final String POSITIONS[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J" };
	final private static int ROWS = 10;
	final private static int COLUMNS = 10;
	// final private static int VECTORS_LENGTH = 9;
	final private static int[][] VECTORS = getFullVectors();
	final private Random random;
	final private static ArrayList<Boat> myBoats = new ArrayList<Boat>(5);
	// public final static int AIRCRAFT_CARRIER_LENGTH = 5;
	// public static int BATTLESHIP_LENGTH = 4;
	// public final static int CRUISER_LENGTH = 3;
	// public final static int SUBMARINE_LENGTH = 3;
	// public final static int DESTROYER_LENGTH = 2;

	final private String[][] grid;

	public Grid() {
		grid = new String[ROWS][COLUMNS];
		random = new Random();
	}

	/**
	 * 
	 */
	private static int[][] getVectors() {
		return new int[][] {
				{ -1, 0 },
				{ 0, -1 }, { 0, 1 },
				{ 1, 0 },
		};
	}

	private static int[][] getFullVectors() {
		return new int[][] {
				{ -1, -1 }, { -1, 0 }, { -1, 1 },
				{ 0, -1 }, { 0, 1 },
				{ 1, -1 }, { 1, 0 }, { 1, 1 }
		};
	}

	/**
	 * @return [row, column]
	 */
	public int[] getRandomPoint() {
		int output[] = new int[2];
		boolean isFreePoint = false;

		while (!isFreePoint) {
			int x = random.nextInt(ROWS);
			int y = random.nextInt(COLUMNS);

			isFreePoint = false;

			for (int[] vector : VECTORS) {
				// vertical border
				// P = [3, 0]
				// v = [-1, 0]
				if (x + vector[0] > ROWS - 1 || x + vector[0] < 0) {
					isFreePoint = true;
				}

				// horizontal border
				if (y + vector[1] > COLUMNS - 1 || y + vector[1] < 0) {
					isFreePoint = true;
				}

				// neighbor not null
				if (!isFreePoint && grid[x + vector[0]][y + vector[1]] == null) {
					isFreePoint = true;
				}

				if (!isFreePoint) {
					break;
				}
			}

			if (!isFreePoint) {
				continue;
			}

			output[0] = x;
			output[1] = y;
		}

		return output;
	}

	/**
	 * 
	 */
	public int[] getRandomVector(int[][] vectors) {
		int i = random.nextInt(vectors.length);
		return vectors[i];
	}

	/**
	 * 
	 */
	public boolean canFillPoint(int x, int y) {
		if (x < 0) {
			// System.out.print("Left overflow\n");
			return false;
		}

		if (x > 9) {
			// System.out.print("Right overflow\n");
			return false;
		}

		if (y < 0) {
			// System.out.print("Top overflow\n");
			return false;
		}

		if (y > 9) {
			// System.out.print("Bottom overflow\n");
			return false;
		}

		for (int[] vector : VECTORS) {
			if (x + vector[0] > 9) {
				continue;
			}

			if (x + vector[0] < 0) {
				continue;
			}

			if (y + vector[1] > 9) {
				continue;
			}

			if (y + vector[1] < 0) {
				continue;
			}

			if (grid[x + vector[0]][y + vector[1]] != null) {
				return false;
			}
		}

		return true;
	}

	/*
	 * 
	 */

	/**
	 * For one boat, get a random point.
	 * Once the point is selected, select a random vector.
	 * Check the next point with the selected vector, if the next point is free, the
	 * next one become the current.
	 * Otherwise, the next point is not free, select another random vector.
	 * If all vectors are consumed, select another point and repeat the loop until
	 * the boat is placed.
	 */
	public void placeBoatRandom(int length, String label) {
		int[] point;
		int[][] coords;
		int[][] vectors;
		boolean canPlace = false;

		// PI = [A, 1]
		// V1 = [1, 0]
		// V2 = [-1, -1]
		// PI + 4 * V1 = [A, 1] + 4 * [1, 0] = [A, 1] + [4, 0] = [A+4, 1+0] = [E, 1]
		// PI + V2 = [A-1, 1-1] = [J, 0]

		while (!canPlace) {
			point = getRandomPoint();
			vectors = getVectors();
			coords = new int[length][2];
			/**
			 * [
			 * [A, 1],
			 * [A, 2],
			 * [A, 3]
			 * ]
			 */

			// gets a vector
			for (int[] vector : vectors) {
				canPlace = true;

				// check if all points can be filled
				for (int i = 0; i < length; i++) {
					if (!canFillPoint(point[0] + i * vector[0], point[1] + i * vector[1])) {
						canPlace = false;
						break;
					}

					coords[i][0] = point[0] + i * vector[0];
					coords[i][1] = point[1] + i * vector[1];
				}

				if (canPlace) {
					for (int i = 0; i < length; i++) {
						grid[coords[i][0]][coords[i][1]] = label;
					}
					break;
				}

			}

		}
	}

	// gestion du positionnement manuelle des bateaux par les joueurs

	// public void placeBoat(int length, String label, Integer x, Integer y, String
	// direction) {
	// int[] point;
	// int[][] coords;
	// int[] vector = getDirectionVector(direction);
	// boolean canPlace;

	// point = getPoint(x, y);
	// coords = new int[length][2];

	// canPlace = true;

	// for (int i = 0; i < length; i++) {
	// if (!canFillPoint(point[0] + i * vector[0], point[1] + i * vector[1])) {
	// canPlace = false;
	// break;
	// }

	// coords[i][0] = point[0] + i * vector[0];
	// coords[i][1] = point[1] + i * vector[1];
	// }

	// if (canPlace) {
	// for (int i = 0; i < length; i++) {
	// grid[coords[i][0]][coords[i][1]] = label;
	// }

	// }

	// }

	// TO DO trouver le décalage en x
	public void placeBoat(Boat boat, Integer x, Integer y, String direction) {
		int[] point;
		int[][] coords;
		int[] vector = getDirectionVector(direction);
		boolean canPlace;

		point = getPoint(x, y);
		coords = new int[boat.type.length][2];

		canPlace = true;

		for (int i = 0; i < boat.type.length; i++) {
			if (!canFillPoint(point[0] + i * vector[0], point[1] + i * vector[1])) {
				canPlace = false;
				break;
			}

			coords[i][0] = point[0] + i * vector[0];
			coords[i][1] = point[1] + i * vector[1];
		}

		if (canPlace) {
			for (int i = 0; i < boat.type.length; i++) {
				grid[coords[i][0]][coords[i][1]] = boat.type.label;
				System.out.println(coords[i][0] + " et " + coords[i][1]);
			}

		}

	}

	public int[] getPoint(Integer x, Integer y) {
		int output[] = new int[2];
		boolean isFreePoint = false;

		while (!isFreePoint) {

			for (int[] vector : VECTORS) {
				// vertical border
				if (x + vector[0] > ROWS - 1 || x + vector[0] < 0) {
					isFreePoint = true;
				}

				// horizontal border
				if (y + vector[1] > COLUMNS - 1 || y + vector[1] < 0) {
					isFreePoint = true;
				}

				// neighbor not null
				if (!isFreePoint && grid[x + vector[0]][y + vector[1]] == null) {
					isFreePoint = true;
				}

				if (!isFreePoint) {
					break;
				}
			}

			if (!isFreePoint) {
				continue;
			}

			output[0] = x;
			output[1] = y;
		}

		return output;
	}

	public int[] getDirectionVector(String direction_vectors) {
		switch (direction_vectors) {
			case "NORTH":
				return new int[] { -1, 0 };
			case "SOUTH":
				return new int[] { 1, 0 };
			case "WEST":
				return new int[] { 0, -1 };
			case "EAST":
				return new int[] { 0, 1 };
		}

		return null;
	}

	/**
	 * 
	 */
	public void show() {
		System.out.print("\n");
		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				// Affiche la lettre sur la première colonne
				if (j == 1) {
					if (i >= 1) {
						System.out.print(POSITIONS[i - 1] + " ");
					}
				}
				// Affiche la position ( chiffre ) sur la première ligne
				if (i == 0) {
					if (j == 0) {
						System.out.print("\\ ");
					}
					if (j >= 1) {
						System.out.printf(" %d ", j);
					}
				} else if (j >= 1 && (grid[i - 1][j - 1] == null)) {
					System.out.print(" • ");
				} else if (j >= 1 && (grid[i - 1][j - 1] != null)) {
					System.out.print(" " + grid[i - 1][j - 1] + " ");
				}
			}
			System.out.print("\n");
		}
	}

	/**
	 * Convert the first coord ( letter A to J ) into a int
	 * 
	 * @param letter
	 * @return the letter convert in integer
	 */
	public int convertCoordLetter(String letter) {
		int coord = 0;
		for (int i = 0; i < POSITIONS.length; i++) {
			if (POSITIONS[i].equals(letter.toUpperCase())) {
				coord = i;
				break;
			}
		}
		return coord;
	}

	public void createBoat(ArrayList<Boat> myBoats) {
		Boat aircraft = new Boat(Boat.typeBoat.AIRCRAFT_CARRIER);
		myBoats.add(aircraft);
		Boat cruiser = new Boat(Boat.typeBoat.CRUISER);
		myBoats.add(cruiser);
		Boat submarine = new Boat(Boat.typeBoat.SUBMARINE);
		myBoats.add(submarine);
		Boat destroyer = new Boat(Boat.typeBoat.DESTROYER);
		myBoats.add(destroyer);
		Boat warship = new Boat(Boat.typeBoat.WARSHIP);
		myBoats.add(warship);
	}

	// VOIR POURQUOI LE CLOSE DU SCANNER PLANTE
	public Object[] askPosition(Object[] position) {
		Scanner input = new Scanner(System.in);
		System.out.println("Veuillez entrez la première coordonée de votre bateau : ");
		String[] result = input.nextLine().split("");
		position[0] = convertCoordLetter(result[0]);
		position[1] = Integer.parseInt(result[1]) - 1;
		System.out.println("Veuillez donnez la direction dans laquelle placer le bateau : ");
		position[2] = input.nextLine();
		return position;
	}

	// fonction pour placer tout les bateaux
	public void placeAllBoat() {
		Object[] position = new Object[3];
		createBoat(myBoats);
		for (Boat b : myBoats) {
			System.out.println(
					"veuillez placer le bateau : " + b.type.name() + ", ce bateau a une taille de " + b.type.length);
			askPosition(position);
			placeBoat(b, (int) position[0], (int) position[1], (String) position[2]);
			show();
		}
	}

}
