
package sodukuvalidate;


import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.HashSet;
import java.io.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.HashSet;
/**
 *
 * @author Malay Lama 
 */
public class Sodukuvalidate {

     
    
    public static void main(String[] args) {
    
    
            
    ReadFile reader = new ReadFile();
    try {
    reader.readSudokuFromFile("/Users/sutlama/Downloads/sudokutest.txt");
    
    // Do something with the sudokuGrid array...
    } catch (FileNotFoundException e) {
    System.out.println("Could not find file: ");
    } catch (IllegalArgumentException e) {
    System.out.println("Invalid input file format.");
    }
    int[][] sudokuGrid = reader.getSudokuGrid();
    sudokuGrid = reader.getSudokuGrid();

    
    reader.printSudokuGrid();
    reader.getSudokuGrid();
    
    
    String outputFileName = "/Users/sutlama/Desktop/resultTesting.txt";

    CheckRow checkRowThread = new CheckRow(1, sudokuGrid, outputFileName);
    checkRowThread.start();
    
    CheckColumn checkColumnThread = new CheckColumn(2, sudokuGrid, outputFileName);
    checkColumnThread.start();
    
    
    String outputFile= "/Users/sutlama/Downloads/resultTesting.txt";
    
    CheckGrid gridChecker = new CheckGrid(3, sudokuGrid, outputFileName);
    gridChecker.start();
    
    
    
    
}


    
    
}
   
    
    
    





class ReadFile {
    private static final int GRID_SIZE = 9;
    private int[][] sudokuGrid = new int[GRID_SIZE][GRID_SIZE];

    public void readSudokuFromFile(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (scanner.hasNextInt()) {
                    sudokuGrid[i][j] = scanner.nextInt();
                } else {
                    throw new IllegalArgumentException("Invalid input file format.");
                }
            }
        }
        scanner.close();
    }

    public int[][] getSudokuGrid() {
        return sudokuGrid;
    }
    
    
     public void printSudokuGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(sudokuGrid[i][j] + " ");
            }
            System.out.println();
        }
    }

}



class CheckRow extends Thread {
    private int id;
    public int[][] sudokuGrid;
    private String outputFileName;

    public CheckRow(int id, int[][] sudokuGrid, String outputFileName) {
        this.id = id;
        this.sudokuGrid = sudokuGrid;
        this.outputFileName = outputFileName;
    }

    @Override
    public void run() {
        try {
            FileWriter writer = new FileWriter(outputFileName, true);
            for (int i = 0; i < 9; i++) {
                boolean isValid = checkRow(i);
                String result = String.format("Thread %d: Row %d %s\n", id, i, isValid ? "is valid" : "is not valid");
                writer.write(result);
                System.out.print(result);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to output file.");
        }
    }

    private boolean checkRow(int rowIndex) {
        boolean[] used = new boolean[10]; // used[0] is not used
        for (int j = 0; j < 9; j++) {
            int value = sudokuGrid[rowIndex][j];
            if (value < 1 || value > 9) {
                return false;
            }
            if (used[value]) {
                return false;
            }
            used[value] = true;
            
        }
        return true;
    }
}





class CheckColumn extends Thread {
    private int id;
    private int[][] sudokuGrid;
    private String outputFileName;

    public CheckColumn(int id, int[][] sudokuGrid, String outputFileName) {
        this.id = id;
        this.sudokuGrid = sudokuGrid;
        this.outputFileName = outputFileName;
    }

    @Override
    public void run() {
        try {
            FileWriter writer = new FileWriter(outputFileName, true);
            for (int j = 0; j < 9; j++) {
                boolean isValid = checkColumn(j);
                String result = String.format("Thread %d: Column %d %s\n", id, j, isValid ? "is valid" : "is not valid");
                writer.write(result);
                System.out.print(result);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to output file.");
        }
    }

    private boolean checkColumn(int colIndex) {
        boolean[] used = new boolean[10]; // used[0] is not used
        for (int i = 0; i < 9; i++) {
            int value = sudokuGrid[i][colIndex];
            if (value < 1 || value > 9) {
                return false;
            }
            if (used[value]) {
                return false;
            }
            used[value] = true;
        }
        return true;
    }
}







class CheckGrid extends Thread {
    private int id;
    private int[][] sudokuGrid;
    private String outputFile;

    public CheckGrid(int id, int[][] sudokuGrid, String outputFile) {
        this.id = id;
        this.sudokuGrid = sudokuGrid;
        this.outputFile = outputFile;
    }

    public void run() {
        try (FileWriter writer = new FileWriter(outputFile, true)) {
            for (int i = 0; i < 9; i += 3) {
                for (int j = 0; j < 9; j += 3) {
                    boolean valid = checkSubGrid(i, j);
                    int subGridNumber = (i / 3) * 3 + (j / 3) + 1;
                    String result = "Thread " + id + ": Subgrid " + subGridNumber + " is " + (valid ? "valid" : "invalid") + "\n";
                    System.out.print(result); // print to console
                    writer.write(result); // write to output text file
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkSubGrid(int row, int col) {
        boolean[] visited = new boolean[9];
        for (int i = row; i < row + 3; i++) {
            for (int j = col; j < col + 3; j++) {
                int num = sudokuGrid[i][j];
                if (num != 0 && visited[num - 1]) {
                    return false;
                }
                visited[num - 1] = true;
            }
        }
        return true;
    }
}



class FileWriterUtil {
    private String filepath;

    public FileWriterUtil(String filepath) {
        this.filepath = filepath;
    }

    public void writeToFile(String content) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
            writer.write(content);
            writer.close();
            System.out.println("Successfully wrote to file: " + filepath);
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file: " + filepath);
            e.printStackTrace();
        }
    }
}