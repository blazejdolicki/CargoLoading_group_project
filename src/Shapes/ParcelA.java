package Shapes;
/**
 * Class representing ParcelA
 */
public class ParcelA extends ParcelShape{
	
	protected static ShapeMaterial material = ShapeMaterial.GREEN;
	protected static int width = 2;
	protected static int height = 2;
	protected static int length = 4;
	protected static String name = "A";
	
	protected static int value = 3;
	/**
         * Create ParcelA
         */
	public ParcelA() {
		super(material, name, value, width, height, length);
	}
	/*
	 * @see Shapes.ParcelShape#clone()
	 */
	@Override
	public ParcelA clone() {
		ParcelA clone = new ParcelA();
		clone.setOrientation(this.orientation);
		return clone;	
	}
        /**
         * Make a clone that has the coordinates
         * @return a clone
         */
        @Override 
        public ParcelA cloneWithCoordinates(){
            ParcelA clone = new ParcelA();
            clone.setOrientation(this.orientation);
            clone.setCurrentCoordinates(this.positionParcelContainer.clone());
            return clone;
        }
}
