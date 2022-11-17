package grid;

import java.util.Random;

/**
 * 
 */
public class Grid {
	final private static int ROWS = 10;
	final private static int COLUMNS = 10;
	final private static int VECTORS_LENGTH = 9;
	final private static int[][] VECTORS = getFullVectors();
	final private Random random;
	
	public final static int AIRCRAFT_CARRIER_LENGTH = 5;
	public final static int BATTLESHIP_LENGTH = 4;
	public final static int CRUISER_LENGTH = 3;
	public final static int SUBMARINE_LENGTH = 3;
	public final static int DESTROYER_LENGTH = 2;

	final private String [][] grid;

	public Grid() {
		grid = new String[ROWS][COLUMNS];
		random = new Random();
	}

	/**
	 * 
	 */
	private static int[][] getVectors() {
		return new int[][] {
			        {-1, 0},
			{0, -1},        { 0, 1},
			        { 1, 0},
		};
	}

	private static int[][] getFullVectors() {
		return new int[][] {
			{-1, -1}, {-1, 0}, {-1, 1},
			{ 0, -1},          { 0, 1},
			{ 1, -1}, { 1, 0}, { 1, 1}
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
				if (x + vector[0] > ROWS -1 || x + vector[0] < 0) {
					isFreePoint = true;
				}

				// horizontal border
				if (y + vector[1] > COLUMNS -1 || y + vector[1] < 0) {
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

		System.out.println(output[0] + ":" + output[1]);

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
			System.out.print("Left overflow\n");
			return false;
		}
		
		if (x > 9) {
			System.out.print("Right overflow\n");
			return false;
		}

		if (y < 0) {
			System.out.print("Top overflow\n");
			return false;
		}
		
		if (y > 9) {
			System.out.print("Bottom overflow\n");
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

	/**
	 * For one boat, get a random point.
	 * Once the point is selected, select a random vector.
	 * Check the next point with the selected vector, if the next point is free, the next one become the current.
	 * Otherwise, the next point is not free, select another random vector.
	 * If all vectors are consumed, select another point and repeat the loop until the boat is placed.
	 */
	public void saveBoat(int length, String label) {
		int[] point;
		int[][] coords;
		int[][] vectors;
		boolean canPlace = false;

		while (!canPlace) {
			point = getRandomPoint();
			vectors = getVectors();
			coords = new int[length][2];
			
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

					// System.out.print(("VECTOR(" + vector[0] + ":" + vector[1] + ") "));
					// System.out.print(point[0] + i * vector[0] + ":" + (point[1] + i * vector[1]) + " \n");
				}

				if (canPlace) {
					for (int i = 0; i < length; i++) {
						// System.out.print(coords[i][0] + ":" + coords[i][1] + " ");
						grid[coords[i][0]][coords[i][1]] = label;
					}
					break;
				}

			}
			
		}
	}

	/**
	 * 
	 */
	public void show() {
		System.out.print("\n");
		for (String[] strings : grid) {
			for (String string : strings) {
				System.out.print((string == null ? "-" : string) + " ");
			}
			System.out.print("\n");
		}
	}
}
