package edu.wpi.cs3733.d19.teamO.request;

public interface IRequest {
    /**
     * Start the service component in a new window on top of the current application.
     *
     * @param xcoord       The X location of the window
     * @param ycoord       The Y location of the window
     * @param windowWidth  The width in pixels of the window
     * @param windowLength The height in pixels of the window
     * @param cssPath      The path to the stylesheet that the request should use
     * @param destNodeID   Where the service request should be done
     * @param originNodeID (If needed) the start location of the service request
     */
    void run(int xcoord, int ycoord, int windowWidth, int windowLength, String cssPath,
             String destNodeID, String originNodeID);
}
