package co.inventorsoft.academy.collections.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;
import java.text.DecimalFormat;

public class Range<T extends Comparable<T>> implements Set<T> {
    private final T start;
    private final T end;
    private final Function<T, T> incrementaFunction;

    public Range(T start, T end, Function<T, T> incrementaFunction) {
        this.start = start;
        this.end = end;
        this.incrementaFunction = incrementaFunction;
    }

    public static <T extends Comparable<T>> Range<T> of(T start, T end, Function<T, T> incrementalFunc) {
        return new Range<>(start, end, incrementalFunc);
    }

    public static Range<Byte> of(Byte start, Byte end) {
        return new Range<>(start, end, step -> (byte) (step + 1));
    }

    public static Range<Short> of(Short start, Short end) {
        return new Range<>(start, end, step -> (short) (step + 1));
    }

    public static Range<Integer> of(Integer start, Integer end) {
        return new Range<>(start, end, step -> step + 1);
    }

    public static Range<Long> of(Long start, Long end) {
        return new Range<>(start, end, step -> step + 1);
    }

    public static Range<Double> of(Double start, Double end) {
        return new Range<>(start, end, step -> step + 0.1d);
    }

    public static Range<Float> of(Float start, Float end) {
        return new Range<>(start, end, step -> step + 0.1f);
    }

    public static Range<Character> of(Character start, Character end) {
        return new Range<>(start, end, step -> (char) (step + 1));
    }

    public int size() {
        if (start instanceof Float || start instanceof Double) {
            Double from = formatDecimal(((Number) start).doubleValue());
            Double to = formatDecimal(((Number) end).doubleValue());
            Double difference = formatDecimal(to - from);

            return (int) (difference * 10) + 1;
        } else if (start instanceof Number){
            long startVal = ((Number) start).longValue();
            long endVal = ((Number) end).longValue();

            long size = (endVal - startVal) + 1;

            return (int) size;
        } else { // for custom types:
            int count = 0;
            T current = start;
            while (current.compareTo(end) <= 0) {
                current = incrementaFunction.apply(current);
                count++;
            }

            return count;
        }
    }

    public boolean isEmpty() {
        return start.equals(end);
    }

    public boolean contains(Object o) {
        if (!(o instanceof Comparable)) {
            return false;
        }

        try {
            T item = (T) o;
            return (item.compareTo(start) >= 0 && item.compareTo(end) <= 0);
        } catch (ClassCastException e) {
            return false;
        }
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) return false;
        }

        return true;
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private T current = start;
            @Override
            public boolean hasNext() {
                return current.compareTo(end) <= 0;
            }

            @Override
            public T next() {
                T result = current;
                current = incrementaFunction.apply(current);

                return result;
            }
        };
    }


    public Object[] toArray() {
        Object[] array = new Object [size()];
        int index = 0;

        for (T t : this) {
            array[index++] = t;
        }

        return array;
    }

    public <T1> T1[] toArray(T1[] a) {
        Object[] array = toArray();

        if (a.length >= array.length) {
            System.arraycopy(array, 0, a, 0, array.length);
            return a;
        } else {
            return (T1[]) Arrays.copyOf(array, a.length, a.getClass());
        }
    }

    public boolean add(T t) {
        throw new UnsupportedOperationException("Unsupported operation! Can't modify a Range");
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Unsupported operation! Can't modify a Range");
    }

    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Unsupported operation! Can't modify a Range");
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported operation! Can't modify a Range");
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unsupported operation! Can't modify a Range");
    }

    public void clear() {
        throw new UnsupportedOperationException("Unsupported operation! Can't modify a Range");
    }

    private Double formatDecimal(Double number) {
        DecimalFormat df = new DecimalFormat("#.#");
        String formattedString = df.format(number);

        return Double.valueOf(formattedString.replace(',', '.'));
    }

}
