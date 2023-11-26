package com.xchess.stockfish.option;

import com.xchess.stockfish.option.impl.BooleanStockfishOption;
import com.xchess.stockfish.option.impl.NumberStockfishOption;
import com.xchess.stockfish.option.impl.StringStockfishOption;

import java.util.*;

public class StockfishOptions {

    private final Map<StockfishOptionKey, Object> optionValues;

    public StockfishOptions() {
        this.optionValues = new EnumMap<>(StockfishOptionKey.class);
    }

    public StockfishOptions(StockfishOptions stockfishOptions) {
        this.optionValues = new EnumMap<>(StockfishOptionKey.class);
        for (Map.Entry<StockfishOptionKey, Object> optionEntry :
                stockfishOptions.getOptionValues().entrySet()) {
            this.setOption(optionEntry.getKey(),
                    optionEntry.getValue());
        }
    }

    public Map<StockfishOptionKey, Object> getOptionValues() {
        return optionValues;
    }

    public StockfishOptions setOption(StockfishOptionKey key, Object value) {
        Optional<StockfishOption> optionRef =
                this.getOptionReference().stream().filter(option -> option.getOptionKey().equals(key)).findFirst();
        if (optionRef.isEmpty()) {
            throw new IllegalArgumentException("Cannot set option with key " + key.getValue());
        }

        optionRef.get().validate(value);
        optionValues.put(key, value);

        return this;
    }

    public StockfishOptions setDefaultOptions() {
        for (StockfishOption optionRef :
                this.getOptionReference()) {
            this.optionValues.put(optionRef.getOptionKey(),
                    optionRef.getDefaultValue());
        }
        return this;
    }

    public StockfishOptions merge(StockfishOptions other) {
        StockfishOptions result = new StockfishOptions(this);
        for (Map.Entry<StockfishOptionKey, Object> optionEntry :
                other.getOptionValues().entrySet()) {
            result = result.setOption(optionEntry.getKey(),
                    optionEntry.getValue());
        }
        return result;
    }

    public List<String> toCommands() {
        List<String> commands = new ArrayList<>();
        for (Map.Entry<StockfishOptionKey, Object> optionEntry :
                this.optionValues.entrySet()) {
            commands.add("setoption name " + optionEntry.getKey().getValue() + " value " + optionEntry.getValue().toString());
        }
        return commands;
    }

    private List<StockfishOption> getOptionReference() {
        List<StockfishOption> optionList = new ArrayList<>();
        optionList.add(new NumberStockfishOption(StockfishOptionKey.THREADS,
                1, 1, 1024));
        optionList.add(new NumberStockfishOption(StockfishOptionKey.HASH, 16,
                1, 33554432));
        optionList.add(new BooleanStockfishOption(StockfishOptionKey.PONDER,
                false));
        optionList.add(new NumberStockfishOption(StockfishOptionKey.MOVE_OVERHEAD, 10, 0, 5000));
        optionList.add(new NumberStockfishOption(StockfishOptionKey.MULTIPV,
                1, 1, 500));
        optionList.add(new NumberStockfishOption(StockfishOptionKey.SKILL_LEVEL, 20, 0, 20));
        optionList.add(new StringStockfishOption(StockfishOptionKey.DEBUG_LOG_FILE, ""));
        optionList.add(new NumberStockfishOption(StockfishOptionKey.SLOW_MOVER, 100, 10, 1000));
        optionList.add(new BooleanStockfishOption(StockfishOptionKey.UCI_CHESS960, false));
        optionList.add(new NumberStockfishOption(StockfishOptionKey.UCI_ELO,
                1320, 1320, 3190));
        optionList.add(new BooleanStockfishOption(StockfishOptionKey.UCI_LIMITSTRENGTH, false));

        return optionList;
    }
}
