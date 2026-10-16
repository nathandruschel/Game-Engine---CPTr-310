package components;

public class SceneryEntity extends Entity{

    public SceneryEntity(int canvasWidth, int canvasHeight) {
        super();
        setXloc(canvasWidth/2.0f);
        setYloc(canvasHeight/2.0f);
        setWrapAround(true);
        setGhost(true);
    }

}
