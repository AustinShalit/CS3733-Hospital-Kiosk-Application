package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

class CSVReader {
  String filename;

  CSVReader() {
    this("PrototypeNodes.csv");
  }

  CSVReader(String filename) {
    this.filename = filename;
  }

  List<Node> getNodesInCSV() {
    LinkedList<Node> nodes = new LinkedList<>();
    try {
      List<String> lines = getFile();


      for (int i = 0; i < lines.size(); i++) {
        if (i > 0) {
          nodes.add(Node.parseNode(lines.get(i)));
        }
      }
    } catch (FileNotFoundException exception) {
      System.out.println("Failed to read from PrototypeNodes.csv, returning two hardcoded nodes.");
      nodes.add(new Node(
          "BCONF00102",
          2150,
          1025,
          2,
          "45 Francis",
          NodeType.CONF,
          "Duncan Reid Conference Room",
          "Conf B0102"
      ));
      nodes.add(new Node(
          "BHALL03802",
          2279,
          786,
          2,
          "45 Francis",
          NodeType.HALL,
          "Hallway Intersection 38 Level 2",
          "Hallway B3802"
      ));
    }
    return nodes;
  }

  private List<String> getFile() throws FileNotFoundException {
    LinkedList<String> lines = new LinkedList<>();

    //Get file from resources folder
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    File file = new File(classLoader.getResource(this.filename).getFile());

    try (Scanner scanner = new Scanner(file, "UTF-8")) {

      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        lines.add(line);
      }

      scanner.close();

    } catch (IOException exception) {
      // exception.printStackTrace();
      throw new FileNotFoundException(); //NOPMD
    }

    return lines;

  }
}
