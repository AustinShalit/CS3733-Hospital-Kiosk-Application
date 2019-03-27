package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.File;
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
    List<String> lines = getFile();

    LinkedList<Node> nodes = new LinkedList<>();

    for (int i = 0; i < lines.size(); i++) {
      if (i > 0) {
        nodes.add(Node.parseNode(lines.get(i)));
      }
    }
    return nodes;
  }

  private List<String> getFile() {
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
      exception.printStackTrace();
    }

    return lines;

  }
}
