package com.banasiak.ambilight.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainColorHistory {

	private static ColorHistoryPanel left;
	private static ColorHistoryPanel right;
	
	public static void main(String[] args) 
	{
		int historySize = 10;
		
		//1. Create the frame.
		JFrame frame = new JFrame("FrameDemo");

		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//3. Create components and put them in the frame.
		//...create emptyLabel...
		frame.getContentPane().add(getPanel(historySize), BorderLayout.CENTER);

		//4. Size the frame.
		
		frame.pack();
		frame.setSize(new Dimension(120, 550));

		//5. Show it.
		frame.setVisible(true);
		
		addRandom(1210, left);
		addRandom(1414, right);
	}
	
	private static Random random = new Random();
	private static Color randomColor() 
	{
		return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
	}
	private static void addRandom(final int delay, final ColorHistoryPanel history)
	{
		Runnable run = new Runnable() 
		{
			@Override
			public void run() 
			{
				boolean done = false;
				
				while (! done) 
				{
					Color color = randomColor();
					history.chpAddToColorList(color);
					System.out.println("Color= " + color);
					try
					{
						Thread.sleep(delay);
					}
					catch (InterruptedException e)
					{
						done = true;
					}
				}
			}
		};
		new Thread(run).start();
		
	}
	
	public static JPanel getPanel(int historySize)
	{
		JPanel ret = new JPanel();
		ret.setLayout(new BorderLayout());
		
		left = new ColorHistoryPanel(historySize);
		right = new ColorHistoryPanel(historySize);
		
		ret.add(left, BorderLayout.WEST);
		ret.add(right, BorderLayout.EAST);
		
		return ret;
	}

}
