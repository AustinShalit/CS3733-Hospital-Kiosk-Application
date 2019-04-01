package edu.wpi.cs3733.d19.teamO.entity.csv;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import edu.wpi.cs3733.d19.teamO.entity.Edge;
import edu.wpi.cs3733.d19.teamO.entity.database.Database;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Read and write a CSV file of Edges.
 */
public class EdgeCsvReaderWriter {

  private final Database database;

  public EdgeCsvReaderWriter(Database database) {
    this.database = database;
  }

  /**
   * Read in the provided file as a List of Edges into the database.
   *
   * @param csv The file to read from
   * @return A list of Edges in the CSV file
   * @throws FileNotFoundException If the file does not exist
   */
  public List<Edge> readEdges(final Path csv) throws IOException {
    checkNotNull(csv);

    return new CsvToBeanBuilder<EdgeVo>(Files.newBufferedReader(csv))
        .withType(EdgeVo.class)
        .build()
        .parse()
        .stream()
        .map(vo -> EdgeVo.toEdge(vo, database))
        .collect(Collectors.toList());
  }

  /**
   * Write provided nodes to file.
   *
   * @param csv   The file to write to
   * @param edges The edges to write
   */
  public void writeNodes(final Path csv, final Collection<Edge> edges) throws IOException,
      CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    checkNotNull(csv);
    checkNotNull(edges);

    StatefulBeanToCsv<EdgeVo> beanToCsv
        = new StatefulBeanToCsvBuilder<EdgeVo>(Files.newBufferedWriter(csv,
        Charset.forName("UTF-8"))).build();
    beanToCsv.write(edges.stream().map(EdgeVo::fromEdge).collect(Collectors.toList()));
  }

  // MUST be public for reflection to work
  public static final class EdgeVo {
    @CsvBindByName(column = "edgeID")
    private String id;
    @CsvBindByName
    private String startNode;
    @CsvBindByName
    private String endNode;

    // MUST be public for reflection to work
    public EdgeVo() {

    }

    private EdgeVo(final String id, final String startNode, final String endNode) {
      this();

      this.id = id;
      this.startNode = startNode;
      this.endNode = endNode;
    }


    private static Edge toEdge(final EdgeVo vo, final Database database) {
      return new Edge(
          vo.id,
          database
              .getNode(vo.startNode)
              .orElseThrow(() -> new IllegalArgumentException("Edge contains unknown node")),
          database
              .getNode(vo.endNode)
              .orElseThrow(() -> new IllegalArgumentException("Edge contains unknown node"))
      );
    }

    private static EdgeVo fromEdge(final Edge edge) {
      return new EdgeVo(
          edge.getEdgeId(),
          edge.getStartNode().getNodeId(),
          edge.getEndNode().getNodeId()
      );
    }
  }
}
