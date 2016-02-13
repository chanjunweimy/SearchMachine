package test;

import static org.junit.Assert.*;
import indexer.Frequency;

import org.junit.Test;

public class TestFrequency {

	@Test
	public void testNormalTextString() {
		String text = "something";
		Frequency freq = new Frequency(text);
		assertTrue("the text is not modified", text.equals(freq.getText()));
		assertTrue("the frequency is set to default", freq.getFrequency() == 0);
		
		freq.incrementFrequency();
		assertTrue("the frequency is incremented as expected", freq.getFrequency() == 1);	
		assertTrue("Frequency string is as expected", freq.toString().equals(text + ":" + 1));
	}
	
	@Test
	public void testNullTextString() {
		String text = null;
		Frequency freq = new Frequency(text);
		assertTrue("the text is not modified", freq.getText() == null);
		assertTrue("the frequency is set to default", freq.getFrequency() == 0);
		
		freq.incrementFrequency();
		assertTrue("the frequency is incremented as expected", freq.getFrequency() == 1);	
		assertTrue("Frequency string is as expected", freq.toString().equals(text + ":" + 1));
	}
	
	@Test
	public void testEmptyTextString() {
		String text = "";
		Frequency freq = new Frequency(text);
		assertTrue("the text is not modified", freq.getText().equals(text));
		assertTrue("the frequency is set to default", freq.getFrequency() == 0);
		
		freq.incrementFrequency();
		assertTrue("the frequency is incremented as expected", freq.getFrequency() == 1);	
		assertTrue("Frequency string is as expected", freq.toString().equals(text + ":" + 1));
	}

	@Test
	public void testNormalTextStringWithFrequency() {
		String text = "something";
		int frequency = 0;
		Frequency freq = new Frequency(text, frequency);
		assertTrue("the text is not modified", text.equals(freq.getText()));
		assertTrue("the frequency is set to default", freq.getFrequency() == 0);
		
		freq.incrementFrequency();
		assertTrue("the frequency is incremented as expected", freq.getFrequency() == 1);	
		assertTrue("Frequency string is as expected", freq.toString().equals(text + ":" + 1));
	}
	
	@Test
	public void testNullTextStringWithFrequency() {
		String text = null;
		int frequency = 0;
		Frequency freq = new Frequency(text, frequency);
		assertTrue("the text is not modified", freq.getText() == null);
		assertTrue("the frequency is set to default", freq.getFrequency() == 0);
		
		freq.incrementFrequency();
		assertTrue("the frequency is incremented as expected", freq.getFrequency() == 1);	
		assertTrue("Frequency string is as expected", freq.toString().equals(text + ":" + 1));
	}
	
	@Test
	public void testEmptyTextStringWithFrequency() {
		String text = "";
		int frequency = 0;
		Frequency freq = new Frequency(text, frequency);
		assertTrue("the text is not modified", freq.getText().equals(text));
		assertTrue("the frequency is set to default", freq.getFrequency() == 0);
		
		freq.incrementFrequency();
		assertTrue("the frequency is incremented as expected", freq.getFrequency() == 1);	
		assertTrue("Frequency string is as expected", freq.toString().equals(text + ":" + 1));
	}
}
