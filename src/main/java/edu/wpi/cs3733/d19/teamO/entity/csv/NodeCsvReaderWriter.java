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

import edu.wpi.cs3733.d19.teamO.entity.Node;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Read and write a CSV file of Nodes.
 */
public class NodeCsvReaderWriter {

  // MUST be public for reflection to work
  public static final class NodeVo {
    @CsvBindByName(column = "nodeID")
    private String nodeId;
    @CsvBindByName
    private int xcoord;
    @CsvBindByName
    private int ycoord;
    @CsvBindByName
    private String floor;
    @CsvBindByName
    private String building;
    @CsvBindByName
    private String nodeType;
    @CsvBindByName
    private String longName;
    @CsvBindByName
    private String shortName;

    // MUST be public for reflection to work
    public NodeVo() {

    }

    private NodeVo(final String nodeId, final int xcoord, final int ycoord, final String floor,
                   final String building, final String nodeType, final String longName,
                   final String shortName) {
      this();

      this.nodeId = nodeId;
      this.xcoord = xcoord;
      this.ycoord = ycoord;
      this.floor = floor;
      this.building = building;
      this.nodeType = nodeType;
      this.longName = longName;
      this.shortName = shortName;
    }

    private static Node toNode(final NodeVo vo) {
      return new Node(
          vo.nodeId,
          vo.xcoord,
          vo.ycoord,
          vo.floor,
          vo.building,
          Node.NodeType.get(vo.nodeType),
          vo.longName,
          vo.shortName
      );
    }

    private static NodeVo fromNode(final Node node) {
      return new NodeVo(
          node.getNodeId(),
          node.getXcoord(),
          node.getYcoord(),
          node.getFloor(),
          node.getBuilding(),
          node.getNodeType().name(),
          node.getLongName(),
          node.getShortName()
      );
    }

  }

  /**
   * Read in the provided file as a List of Nodes.
   *
   * @param csv The file to read from
   * @return A list of Nodes in the CSV file
   * @throws FileNotFoundException If the file does not exist
   */
  public List<Node> readNodes(final Path csv) throws IOException {
    checkNotNull(csv);

    return new CsvToBeanBuilder<NodeVo>(Files.newBufferedReader(csv))
        .withType(NodeVo.class)
        .build()
        .parse()
        .stream()
        .map(NodeVo::toNode)
        .collect(Collectors.toList());
  }

  /**
   * Write provided nodes to file.
   *
   * @param csv   The file to write to
   * @param nodes The nodes to write
   */
  public void writeNodes(final Path csv, final Collection<Node> nodes) throws IOException,
      CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    checkNotNull(csv);
    checkNotNull(nodes);

    StatefulBeanToCsv<NodeVo> beanToCsv
        = new StatefulBeanToCsvBuilder<NodeVo>(Files.newBufferedWriter(csv,
        Charset.forName("UTF-8"))).build();
    beanToCsv.write(nodes.stream().map(NodeVo::fromNode).collect(Collectors.toList()));
  }
}
