package joystick;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import joystick.event.JoystickEvent;
import joystick.event.JoystickEvent.EventType;

import org.lwjgl.glfw.GLFW;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Jul 12, 2015, 10:09:38 PM 
 */
public class Joystick {

	private List<Boolean> pBS = new ArrayList<>();
	
	private List<Boolean> cBS = new ArrayList<>();
	
	private List<Float> pAS = new ArrayList<>();
	
	private List<Float> cAS = new ArrayList<>();
	
	private String name;
	
	private int buttonCount;
	
	private int num;
	
	private JoystickManager manager;
	
	private float dzMax = .15f;
	
	private float dzMin = -.15f;
	
	public Joystick( JoystickManager manager, int num, String name, int buttonCount ) {
		this.num = num;
		this.name = name;
		this.buttonCount = buttonCount;
		this.manager = manager;
	}
	
	public void updateAxisState( FloatBuffer b ) {
		pAS = cAS;
		cAS = new ArrayList<>();
		if ( b != null ) {
			for ( int i = 0; i < b.capacity(); i++ ) {
				float f = b.get( i );
				cAS.add( f > dzMax || f < dzMin ? f : 0f );
			}
		}
		generateAxisEvents();
	}
	
	public void updateButtonState( ByteBuffer b ) {
		pBS = cBS;
		cBS = new ArrayList<>();
		if ( b != null ) {
			for ( int i = 0; i < buttonCount; i++ ) {
				cBS.add( b.get( i ) == GLFW.GLFW_PRESS ? true : false );
			}
		}
		generateButtonEvents();
	}
	
	private void generateAxisEvents() {
		if ( !pAS.isEmpty() ) {
			for ( int i = 0; i < cAS.size(); i++ ) {
				float p = pAS.get( i );
				float c = cAS.get( i );
				if ( c != p ) {
					manager.notifyListeners( new JoystickEvent( this, EventType.AXIS_MOVED, i, c, c - p ) );
				}
			}
		}
	}
	
	private void generateButtonEvents() {
		if ( !pBS.isEmpty() ) {
			for ( int i = 0; i < cBS.size(); i++ ) {
				boolean p = pBS.get( i );
				boolean c = cBS.get( i );
				EventType et = null;
				if ( p && c ) {
					et = EventType.BUTTON_HELD;
				} else if ( p && !c ) {
					et = EventType.BUTTON_RELEASED;
				} else if ( !p && c ) {
					et = EventType.BUTTON_PRESSED;
				} else {
					et = null;
				}
				if ( et != null ) {
					manager.notifyListeners( new JoystickEvent( this, et, i ) );
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + buttonCount;
		result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
		result = prime * result + num;
		return result;
	}

	public boolean equals( Object obj ) {
		boolean ret = false;
		if ( obj != null && obj instanceof Joystick ) {
			Joystick o = (Joystick)obj;
			ret =  num == o.getNum() &&  name.equals( o.getName() ) && buttonCount == o.getButtonCount();
		}
		return ret;
	}
	
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the buttonCount
	 */
	public int getButtonCount() {
		return buttonCount;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	public String toString() {
		String ret = "";
		ret += "-----------------\n";
		ret += num + ": " + name + "\n";
		ret += "Button Count: " + buttonCount + "\n";
		ret += "-----------------\n";
		return ret;
	}
}