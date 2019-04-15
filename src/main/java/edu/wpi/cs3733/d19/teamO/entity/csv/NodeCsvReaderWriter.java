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

import edu.wpi.cs3733.d19.teamO.entity.Node;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Read and write a CSV file of Nodes.
 */
public class NodeCsvReaderWriter {

  /**
   * Read in the provided file as a List of Nodes.
   *
   * @param reader The input stream to read from
   * @return A list of Nodes in the CSV file
   */
  public List<Node> readNodes(final Reader reader) {
    checkNotNull(reader);

    return new CsvToBeanBuilder<NodeVo>(reader)
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
   * @param writer The output stream to write to
   * @param nodes  The nodes to write
   */
  public void writeNodes(final Writer writer, final Collection<Node> nodes)
      throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
    checkNotNull(writer);
    checkNotNull(nodes);

    StatefulBeanToCsv<NodeVo> beanToCsv
        = new StatefulBeanToCsvBuilder<NodeVo>(writer).build();
    beanToCsv.write(nodes.stream().map(NodeVo::fromNode).collect(Collectors.toList()));
  }

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
    @CsvBindByName
    private String polygonCoords;

    // MUST be public for reflection to work
    public NodeVo() {

    }

    public String getNodeId() {
      return nodeId;
    }

    public int getXcoord() {
      return xcoord;
    }

    public int getYcoord() {
      return ycoord;
    }

    public String getFloor() {
      return floor;
    }

    public String getBuilding() {
      return building;
    }

    public String getNodeType() {
      return nodeType;
    }

    public String getLongName() {
      return longName;
    }

    public String getShortName() {
      return shortName;
    }

    public String getPolygonCoords() {
      return polygonCoords;
    }

    private NodeVo(final String nodeId, final int xcoord, final int ycoord, final String floor,
                   final String building, final String nodeType, final String longName,
                   final String shortName, final String polygonCoords) {
      this();

      this.nodeId = nodeId;
      this.xcoord = xcoord;
      this.ycoord = ycoord;
      this.floor = floor;
      this.building = building;
      this.nodeType = nodeType;
      this.longName = longName;
      this.shortName = shortName;
      this.polygonCoords = polygonCoords;
    }

    private static Node toNode(final NodeVo vo) {
      System.out.println(vo.polygonCoords);
      return new Node(
          vo.nodeId,
          vo.xcoord,
          vo.ycoord,
          vo.floor,
          vo.building,
          Node.NodeType.get(vo.nodeType),
          vo.longName,
          vo.shortName,
          Node.parsePolygonFromString(vo.polygonCoords)
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
          node.getShortName(),
          node.polygonCoordsToString()
      );
    }

  }
}
