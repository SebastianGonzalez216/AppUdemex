package tomas.garza;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.Position;
import javax.swing.text.View;

public class SubrayadorTexto extends DefaultHighlighter.DefaultHighlightPainter {

	private static final BasicStroke OO_STROKE1 = new BasicStroke(1.0F, 1, 1, 10.0F, new float[] { 3.0F, 5.0F }, 2.0F);

	private static final BasicStroke OO_STROKE2 = new BasicStroke(1.0F, 1, 1, 10.0F, new float[] { 1.0F, 3.0F }, 3.0F);

	private static final BasicStroke OO_STROKE3 = new BasicStroke(1.0F, 1, 1, 10.0F, new float[] { 3.0F, 5.0F }, 6.0F);

	private static final BasicStroke ZIGZAG_STROKE1 = new BasicStroke(1.0F, 1, 1, 10.0F, new float[] { 1.0F, 1.0F },
			0.0F);

	private final Color underlineColor;

	private final Color backgroundColor;

	public SubrayadorTexto(Color backgroundColor, Color underlineColor) {
		super(backgroundColor);
		this.backgroundColor = backgroundColor;
		this.underlineColor = underlineColor;
	}

	public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
		Rectangle rect;
		if (this.backgroundColor != null)
			super.paintLayer(g, offs0, offs1, bounds, c, view);
		if (offs0 == view.getStartOffset() && offs1 == view.getEndOffset()) {
			if (bounds instanceof Rectangle) {
				rect = (Rectangle) bounds;
			} else {
				rect = bounds.getBounds();
			}
		} else {
			try {
				Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
				rect = (shape instanceof Rectangle) ? (Rectangle) shape : shape.getBounds();
			} catch (BadLocationException e) {
				rect = null;
			}
		}
		if (rect != null) {
			Color color = this.underlineColor;
			if (color == null) {
				g.setColor(c.getSelectionColor());
			} else {
				g.setColor(color);
			}
			rect.width = Math.max(rect.width, 1);
			int descent = c.getFontMetrics(c.getFont()).getDescent();
			if (descent > 3) {
				drawCurvedLine(g, rect);
			} else if (descent > 2) {
				drawCurvedLine(g, rect);
			} else {
				drawLine(g, rect);
			}
		}
		return rect;
	}

	private void drawCurvedLine(Graphics g, Rectangle rect) {
		int x1 = rect.x;
		int x2 = rect.x + rect.width;
		int y = rect.y + rect.height;
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setStroke(OO_STROKE1);
		g2.drawLine(x1, y - 1, x2, y - 1);
		g2.setStroke(OO_STROKE2);
		g2.drawLine(x1, y - 2, x2, y - 2);
		g2.setStroke(OO_STROKE3);
		g2.drawLine(x1, y - 3, x2, y - 3);
	}

	private void drawLine(Graphics g, Rectangle rect) {
		int x1 = rect.x;
		int x2 = rect.x + rect.width;
		int y = rect.y + rect.height;
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(ZIGZAG_STROKE1);
		g2.drawLine(x1, y - 1, x2, y - 1);
	}
}