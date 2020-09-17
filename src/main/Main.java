package main;

//import java.util.Arrays;

public class Main {
	MainFrame frame;
	
	public static void main(String[] args) {
//		var list = Utils.findAllOccurrences("CSASTPKVSIQUTGQUCSASTPIUAQJB", 0);
//		for(int i = 1; i < "CSASTPKVSIQUTGQUCSASTPIUAQJB".length(); i++) {
//			list.addAll(Utils.findAllOccurrences("CSASTPKVSIQUTGQUCSASTPIUAQJB", i));
//		}
//		for(var item : list) {
//			System.out.println("Sequence: " + item.getObj1() + ", Positions: " + Arrays.toString(item.getObj2()));
//		}
//		
//		var kasiski = AnalysisUtils.KasiskiTest("CSASTPKVSIQUTGQUCSASTPIUAQJB");
//		for(var item : kasiski) {
//			System.out.println("Sequence: " + item.sequence + ", Spacing: " + item.spacing);
//		}
		new Main().init();
	}
	
	public void init() {
		frame = new MainFrame();
	}
}