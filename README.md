# Java Chess Stockfish

Java Chess Stockfish provides a java integration for the Stockfish chess engine.

## Installation

```xml

<dependency>
    <groupId>io.github.guillaumcn</groupId>
    <artifactId>chess-stockfish</artifactId>
    <version>1.0.4</version>
</dependency>

```

Add maven dependency in your *pom.xml* file.

## Usage

```java
public class Main {

    public static void main(String[] args) throws IOException,
            TimeoutException {
        // Linux Mac
        ProcessWrapper pw = new ProcessWrapper("path/to/stockfish/engine");
        // Windows
        ProcessWrapper pw = new ProcessWrapper("cmd", "path\\to\\stockfish\\engine.exe");
        StockfishConfig sc = new StockfishConfig().setTimeoutInMs(5000);
        Stockfish stockfish = new Stockfish(pw, sc);

        // Example of use

        // Update options
        stockfish.setOptions(StockfishOptions.builder().threads(20).build());
        // Move to start position
        stockfish.moveToStartPosition(true);
        // This will show a list of possible moves from current position
        System.out.println(stockfish.getPossibleMoves());
        // This will show a list of possible moves from current position for
        // a specific square
        System.out.println(stockfish.getPossibleMoves("a2"));
        // This will show the best move from current position
        EvaluationParameters ep = EvaluationParameters.builder().depth(10).build();
        System.out.println(stockfish.findBestMove(ep));
        // This will analyze current position
        EvaluationParameters ep2 = EvaluationParameters.builder().depth(10).build();
        System.out.println(stockfish.getPositionEvaluation(ep2));
    }
}
```

## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request

## History

* Version 1.0.0 (2023-12-02) - First release
* Version 1.0.1 (2023-12-05) - Make Stockfish integration thread safe
* Version 1.0.2 (2024-01-03) - Use lombok for project
* Version 1.0.3 (2024-01-03) - Fixed build and publication
* Version 1.0.4 (2024-05-07) - Added custom exceptions

## Credits

Developer - Guillaume Lerda (@guillaumcn)

Feel free to add issue if needed a new functionality.

## License

The MIT License (MIT)

Copyright (c) 2024 Guillaume Lerda

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.