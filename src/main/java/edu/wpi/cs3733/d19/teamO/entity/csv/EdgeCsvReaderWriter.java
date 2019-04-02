package edu.wpi.cs3733.d19.teamO.entity.csv;

import java.io.Reader;
import java.io.Writer;
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

  private final Database database;

  public EdgeCsvReaderWriter(Database database) {
    this.database = database;
  }

  /**
   * Read in the provided file as a List of Edges into the database.
   *
   * @param reader The input stream to read from
   * @return A list of Edges in the CSV file
   */
  public List<Edge> readEdges(final Reader reader) {
    checkNotNull(reader);

    return new CsvToBeanBuilder<EdgeVo>(reader)
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
   * @param writer The output stream to write to
   * @param edges The edges to write
   */
  public void writeNodes(final Writer writer, final Collection<Edge> edges)
      throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    checkNotNull(writer);
    checkNotNull(edges);

    StatefulBeanToCsv<EdgeVo> beanToCsv
        = new StatefulBeanToCsvBuilder<EdgeVo>(writer).build();
    beanToCsv.write(edges.stream().map(EdgeVo::fromEdge).collect(Collectors.toList()));
  }
}
