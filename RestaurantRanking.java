
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class RestaurantRanking {

	private String getShortlistedRestaurants() throws IOException {
		Scanner scanner = new Scanner(System.in);
		String userInput = new String("");

		System.out.print("Number of shortlisted restaurants, N:	");
		int numberOfRestaurants = scanner.nextInt();

		System.out.println("Please enter the TH ranks.");
		scanner.reset();
		while (numberOfRestaurants > 0) {
			String inputLine = scanner.nextLine();
			if (inputLine.isEmpty()) {
				continue;
			}
			if (!inputLine.startsWith("I ") && !inputLine.startsWith("R ")) {
				System.err.println("Invalid format; Please enter TH rank again.");
				continue;
			}

			int rank = Integer.parseInt(inputLine.replace("I", "").replace("R", "").trim());
			if (rank < 1 || rank > 20) {
				System.err.println("Invalid rank; Please enter TH rank again.");
				continue;
			}

			userInput = userInput + inputLine + "\n";
			numberOfRestaurants--;
		}

		writeShortListedRestaurantsToFile(userInput);

		return readFileText("shortlistedRestaurants.txt");
	}

	private void writeShortListedRestaurantsToFile(String inputText) throws IOException {
		File shortlistedRestaurants = new File("shortlistedRestaurants.txt");
		FileOutputStream shortlistedOutputStream = new FileOutputStream(shortlistedRestaurants);
		PrintWriter writer = new PrintWriter(shortlistedOutputStream);
		writer.append(inputText);
		writer.close();
		shortlistedOutputStream.close();
	}

	private int[] getIsbRestaurants(final String fileText) {
		String[] lines = fileText.split("\n");
		int numberOfRestaurants = 0;
		for (String line : lines) {
			if (line.startsWith("I ")) {
				numberOfRestaurants++;
			}
		}

		int[] isbRestaurants = new int[numberOfRestaurants];
		int addedRestaurants = 0;
		for (String line : lines) {
			if (line.startsWith("I ")) {
				line = line.replace("I ", "");
				isbRestaurants[addedRestaurants] = Integer.parseInt(line.trim());
				addedRestaurants++;
			}
		}
		return isbRestaurants;
	}

	private int[] getRwpRestaurants(String fileText) {
		String[] lines = fileText.split("\n");
		int numberOfRestaurants = 0;
		for (String line : lines) {
			if (line.startsWith("R ")) {
				numberOfRestaurants++;
			}
		}

		int[] isbRestaurants = new int[numberOfRestaurants];
		int addedRestaurants = 0;
		for (String line : lines) {
			if (line.startsWith("R ")) {
				line = line.replace("R ", "");
				isbRestaurants[addedRestaurants] = Integer.parseInt(line.trim());
				addedRestaurants++;
			}
		}
		return isbRestaurants;
	}

	private int[] desSort(int unsortedArray[]) {
		int[] sortedArray = unsortedArray;
		for (int keyIndex = 1; keyIndex < sortedArray.length; keyIndex++) {
			int key = sortedArray[keyIndex];
			int predecessorIndex = keyIndex;
			while (predecessorIndex > 0 && sortedArray[predecessorIndex - 1] < key) {
				sortedArray[predecessorIndex] = sortedArray[predecessorIndex - 1];
				predecessorIndex--;
			}
			sortedArray[predecessorIndex] = key;
		}
		return sortedArray;
	}

	private String getFinalList(int[] isbRestaurants, int[] rwpRestaurants) throws IOException {
		File finalRestaurents = new File("finalist.txt");
		FileOutputStream outputStream = new FileOutputStream(finalRestaurents);
		PrintWriter writer = new PrintWriter(outputStream);

		for (int i = 0; i < isbRestaurants.length; i++) {
			writer.append(isbRestaurants[i] + "").append(" ");
		}
		writer.flush();

		for (int i = 0; i < rwpRestaurants.length; i++) {
			writer.append(rwpRestaurants[i] + "").append(" ");
		}

		writer.close();
		outputStream.close();

		return readFileText("finalist.txt");
	}

	private String readFileText(String fileName) throws IOException {
		File shortlistedRestaurants = new File(fileName);
		String fileText = new String();
		try (FileInputStream fis = new FileInputStream(shortlistedRestaurants)) {
			int i = 0;
			do {
				byte[] buf = new byte[1024];
				i = fis.read(buf);

				String value = new String(buf);
				fileText += value;
			} while (i != -1);
		}
		return fileText;
	}

	public static void main(String[] args) {
		try {
			RestaurantRanking restaurantRanking = new RestaurantRanking();
			String inputRestaurantsList = restaurantRanking.getShortlistedRestaurants();

			int[] isbRestaurants = restaurantRanking.getIsbRestaurants(inputRestaurantsList);
			int[] rwpRestaurants = restaurantRanking.getRwpRestaurants(inputRestaurantsList);

			int[] sortedIsbRestaurants = restaurantRanking.desSort(isbRestaurants);
			int[] sortedRwpRestaurants = restaurantRanking.desSort(rwpRestaurants);

			String finalList = restaurantRanking.getFinalList(sortedIsbRestaurants, sortedRwpRestaurants);
			;

			System.out.println("The final list is:");
			System.out.println(finalList);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
