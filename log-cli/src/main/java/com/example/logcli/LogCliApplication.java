package com.example.logcli;



import com.example.logcli.Models.CliArgs;
import com.example.logcli.Models.LogEntry;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.lang.System.*;

public class LogCliApplication {

	public static void main(String[] args) {
		int exitCode = new LogCliApplication().run(args);
		exit(exitCode);
	}

	int run(String[] args) {
		try {
			CliArgs cliArgs = CliArgs.parse(args);

			LogParser parser = new LogParser();
			LogAnalyzer analyzer = new LogAnalyzer();

			var entries = parser.parse(Path.of(cliArgs.file()));

			Map<String, Long> top =
					analyzer.topNEndpoints(entries, cliArgs.since(), cliArgs.topN());

			printResults(top, cliArgs, analyzer, entries);
			return 0;

		} catch (Exception e) {
			err.println("Error: " + e.getMessage());
			return 1;
		}
	}

	private void printResults(
			Map<String, Long> top,
			CliArgs args,
			LogAnalyzer analyzer,
			List<LogEntry> entries
	) {
		out.println("Top " + args.topN() + " endpoints since " + args.since());
		top.forEach((k, v) -> out.println(k + " -> " + v + " hits"));

		out.println("\nResponse time percentiles:");
		for (int p = 10; p <= 100; p += 10) {
			out.println("p" + p + " = " + analyzer.percentile(entries, p) + " ms");
		}
	}

}
