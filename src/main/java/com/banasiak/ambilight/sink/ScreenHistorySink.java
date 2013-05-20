package com.banasiak.ambilight.sink;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.banasiak.ambilight.api.ContextProvider;
import com.banasiak.ambilight.gui.ColorHistoryPanel;
import com.banasiak.ambilight.model.TwoChannelColorData;

public class ScreenHistorySink implements AmbilightSink
{
	private final int historySize;
	private ColorHistoryPanel left;
	private ColorHistoryPanel right;
	private JFrame frame;
	
	public ScreenHistorySink(int historySize)
	{
		this.historySize = historySize;

		create();
		
		frame.setVisible(true);
		
	}
	private void create()
	{
		//1. Create the frame.
		frame = new JFrame("FrameDemo");

		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//3. Create components and put them in the frame.
		//...create emptyLabel...
		frame.getContentPane().add(getPanel(historySize), BorderLayout.CENTER);

		//4. Size the frame.
		
		frame.pack();
		frame.setSize(new Dimension(120, 550));

		//5. Show it.
		//frame.setVisible(true);
	}
	
	
	private JPanel getPanel(int historySize)
	{
		JPanel ret = new JPanel();
		ret.setLayout(new BorderLayout());
		
		left = new ColorHistoryPanel(historySize);
		right = new ColorHistoryPanel(historySize);
		
		ret.add(left, BorderLayout.WEST);
		ret.add(right, BorderLayout.EAST);
		
		return ret;
	}

	
	
	
	
	@Override
	public TwoChannelColorData accept(TwoChannelColorData input, ContextProvider<AmbilightSinkContext> contextProvider) 
	{
		System.out.println("SHS .left=" + left + " .right=" + right + " input=" + input);
		left.chpAddToColorList(input.getFirst());
		right.chpAddToColorList(input.getSecond());
		
		return input;
	}

}
