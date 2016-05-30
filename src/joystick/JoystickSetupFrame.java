package joystick;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import joystick.event.JoystickEvent;
import joystick.event.JoystickEventListener;

/**
 * @author Daniel J. Rivers
 *         2015
 *
 * Created: Jul 12, 2015, 7:55:56 PM 
 */
public class JoystickSetupFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	private JPanel p;
	
	public JoystickSetupFrame() {
		this.setSize( new Dimension( 400, 400 ) );
		this.setLayout( new BorderLayout() );
		p = new JPanel();
		this.add( p, BorderLayout.CENTER );
		JoystickManager jt = new JoystickManager();
		jt.addListener( new JoystickEventListener() {
			@Override
			public void buttonReleased( JoystickEvent e ) {
				System.out.println( "RELEASED BUTTON: " + e.getButton() );
				if ( e.getButton() == 0 ) {
					p.setBackground( Color.GREEN );
				} else if ( e.getButton() == 1 ) {
					p.setBackground( Color.RED );
				} else if ( e.getButton() == 2 ) {
					p.setBackground( Color.BLUE );
				} else if ( e.getButton() == 3 ) {
					p.setBackground( Color.YELLOW );
				}
				
			}
			
			@Override
			public void buttonPressed( JoystickEvent e ) {
				System.out.println( "PRESSED BUTTON: " + e.getButton() );
			}
			
			@Override
			public void buttonHeld( JoystickEvent e ) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connected( JoystickEvent e ) {
				System.out.println( "NEW JOYSTICK DETECTED: " + e.getJoystick().toString() );
			}

			@Override
			public void disconnected( JoystickEvent e ) {
				System.out.println( "JOYSTICK DISCONNECTED: " + e.getJoystick().toString() );
			}

			@Override
			public void axisMoved( JoystickEvent e ) {
				System.out.println( "MOVED AXIS " + e.getAxis() + ":  Val=" + e.getAxisValue() + "  Delta=" + e.getAxisDelta() );
				
			}
		} );
		this.setVisible( true );
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowOpened( WindowEvent e ) {
			}
			
			@Override
			public void windowClosing( WindowEvent e ) {
				e.getWindow().dispose();
				jt.destroy();
			}
			
			@Override
			public void windowClosed( WindowEvent e ) {
			}
			
			@Override
			public void windowActivated( WindowEvent e ) {
				jt.startPolling();
			}
			
			@Override
			public void windowDeactivated( WindowEvent e ) {
				jt.stopPolling();
			}
		} );	
	}
	
	public static void main( String[] args ) {
		new JoystickSetupFrame();
	}	
}