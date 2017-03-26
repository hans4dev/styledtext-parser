package pdf;

public interface StyledTextParsable {

	void startTextStyle(StyledText styledText);	
	void endTextStyle(StyledText styledText);
	
	void startTextLine(StyledText styledText);
	void endTextLine(StyledText styledText);
}
