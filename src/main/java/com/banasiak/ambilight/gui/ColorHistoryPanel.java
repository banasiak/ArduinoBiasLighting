package com.banasiak.ambilight.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ColorHistoryPanel extends JComponent {

	private Color defaultColor = Color.black;
	private int colorListSize;
	private List<Color> colorList = new ArrayList<Color>();
	
	public ColorHistoryPanel(int historySize)
	{
		this.colorListSize = historySize;
		chpResetColorList();
		setPreferredSize(getPreferredSize());
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(20, 20);
	}
	
	public void chpResetColorList() 
	{
		List<Color> replace = new ArrayList<Color>(colorListSize);
		for (int index = 0, n = colorListSize; index < n; index++) 
		{
			replace.add(defaultColor);
		}
		setColorList(replace);
	}
	public void chpAddToColorList(Color add) 
	{
		List<Color> replace = new ArrayList<Color>(colorListSize);
		replace.addAll(colorList);
		replace.remove(0);
		replace.add(add);
		setColorList(replace);
	}
	
	private void setColorList(List<Color> newlist)
	{
		colorList = newlist;
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();
        
        List<Color> list = colorList;
        
        int gridWidth = d.width / 1;
        int gridHeight = d.height / list.size();

        System.out.println("D=" + d + " gw=" + gridWidth + " gh=" + gridHeight);
        
        int x = 0;
        int y = 0;

        for (int index = 0, n = list.size(); index < n; index++) 
        {
            g2.setColor(list.get(index));
        	g2.fillRect(x, y, gridWidth, gridHeight);
        	x += 0;
        	y += gridHeight;
        }
	}
	
}
