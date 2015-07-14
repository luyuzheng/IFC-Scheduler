import gui.main.MainWindow;
import gui.sub.CrashReportUI;

/** Runs the scheduler application. */
public class Main {
	public static void main(String[] args) {
		try {
			new MainWindow("IFC Scheduler");
		} catch (Exception e) {
			CrashReportUI.ShowDialog(e);
		}
	}
}
