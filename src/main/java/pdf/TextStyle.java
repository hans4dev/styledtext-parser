package pdf;

import com.itextpdf.text.pdf.parser.TextRenderInfo;

public class TextStyle {
	
	private String text;
	private String fontName;
	private float fontSize;
	private String colorHex;
	private boolean isHardReturn;
	
	private TextStyle lastStyle;

	
	public TextStyle() {
		// for cloning
	}
	
	public TextStyle(TextRenderInfo renderInfo) {
		updateRenderInfo(renderInfo);
	}
	
	public TextStyle(TextRenderInfo renderInfo, boolean isHardReturn) {
    	this(renderInfo);
		this.isHardReturn = isHardReturn;
	}
	
	private void historize() {
        // historize renderInfo
		try {
			lastStyle = (TextStyle) this.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isChanged() {
		return !this.equals( lastStyle );
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public String getColorHex() {
		return colorHex;
	}

	public void setColorHex(String colorHex) {
		this.colorHex = colorHex;
	}

	public boolean isHardReturn() {
		return isHardReturn;
	}

	public void setHardReturn(boolean isHardReturn) {
		this.isHardReturn = isHardReturn;
	}

	public TextStyle getLastLayoutInfo() {
		return lastStyle;
	}

	public void setLastLayoutInfo(TextStyle lastLayoutInfo) {
		this.lastStyle = lastLayoutInfo;
	}
	
	@Override
	public String toString() {
		return String.format("%s, %s, %d, %s, %s", text, fontName, Math.round( fontSize ), colorHex, isHardReturn);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof TextStyle) {
			TextStyle otherLayoutInfo = (TextStyle) obj;
			
	    	boolean isEqual = false;
	    	if (otherLayoutInfo.fontName != null) {
	    		isEqual |= otherLayoutInfo.fontName.equals( this.fontName );
	    	}
	    	isEqual |= otherLayoutInfo.fontSize == this.fontSize;
	    	if (otherLayoutInfo.colorHex != null) {
	    		isEqual |= otherLayoutInfo.colorHex.equals( this.colorHex );
	    	}
	    	
	    	return isEqual;
		}
		return false;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		TextStyle clonedLayoutInfo = new TextStyle();
		clonedLayoutInfo.colorHex = colorHex;
		clonedLayoutInfo.fontName = fontName;
		clonedLayoutInfo.fontSize = fontSize;
		clonedLayoutInfo.isHardReturn = isHardReturn;
		clonedLayoutInfo.text = text;
		
		return clonedLayoutInfo;
	}

	public void updateRenderInfo(TextRenderInfo renderInfo) {
		historize();
		
		this.text = renderInfo.getText();
		if (renderInfo.getFont() != null) {
			this.fontName = renderInfo.getFont().getPostscriptFontName();
		}
    	this.fontSize = TextRenderUtil.getFontSize(renderInfo);
    	this.colorHex = Integer.toHexString( renderInfo.getFillColor().getRGB() );		
	}
    

}
