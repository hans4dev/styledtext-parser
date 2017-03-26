package pdf;

import java.io.PrintStream;

public class StyledText {
	private TextStyle style;
	private StringBuilder text;
	
	public StyledText() {
		text = new StringBuilder();
		style = new TextStyle();
	}
	
	public void appendText(String text) {
		this.text.append(text);		
	}

	public void appendText(char c) {
		this.text.append(c);		
	}
	
	public void println(PrintStream out) {
		out.println(this);
	}
	
	public TextStyle getStyle() {
		return style;
	}
	public void setStyle(TextStyle style) {
		this.style = style;
	}
	
	public StringBuilder getText() {
		return text;
	}
	public void setText(StringBuilder text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return String.format("\"%s\", Style: %s", this.text, style);
	}
	
}
