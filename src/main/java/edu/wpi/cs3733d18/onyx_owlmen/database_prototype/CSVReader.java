package edu.wpi.cs3733d18.onyx_owlmen.database_prototype;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

class CSVReader {
  String filename;

  CSVReader() {
    this("/PrototypeNodes.csv");
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

    InputStream in = getClass().getResourceAsStream(this.filename);
    InputStreamReader isr = null;

    try {
      isr = new InputStreamReader(in, "UTF-8");
    } catch (UnsupportedEncodingException exception) {
      exception.printStackTrace();
      throw new FileNotFoundException(); // NOPMD
    }

    BufferedReader reader = new BufferedReader(isr);
    String cur;
    try {
      cur = reader.readLine();
      while (cur != null) {
        lines.add(cur);
        cur = reader.readLine();
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }

    return lines;

  }
}
