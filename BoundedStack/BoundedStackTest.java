package BoundedStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoundedStackTest {
    static int passed = 0, failed = 0;

    static void check(String name, boolean ok) {
        if (ok) {
            passed++;
            System.out.println("  [PASS] " + name);
        } else {
            failed++;
            System.out.println("  [FAIL] " + name);
        }
    }

    public static void main(String[] args) {
        boolean assertsOn = false;
        assert assertsOn = true;
        if (!assertsOn) {
            System.out.println("WARNING: assertions disabled"
                    + " - re-run with: java -ea PlaylistTest\n");
        }

        System.out.println("=== BoundedStack Test Suite ===\n");

        testCreators();
        testAdd();
        testRemove();
        testObservers();
        testProducer();
        testExposure();

        System.out.println("\n=== Summary ===");
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
        System.out.println("Total : " + (passed + failed));
        System.out.println(failed == 0 ? "ALL TESTS PASSED" : "SOME TESTS FAILED");

        if (failed > 0) {
            System.exit(1);
        }
    }

    // --- Partition: ว่าง / มีข้อความ / input ที่ผิดเงื่อนไข ---
    private static void testCreators() {
        System.out.println("-- Creators --");

        // กรณี list ว่างเปล่า
        BoundedStack empty = new BoundedStack();
        check("new() -> empty", empty.size() == 0);
        check("new() -> contains nothing", !empty.contains("anything"));

        // กรณี สร้าง list ที่ได้มาจากผู้ใช้งาน
        BoundedStack p = new BoundedStack(Arrays.asList("Hello", "World", "ComSci"));
        check("new(list) -> size 3", p.size() == 3);
        check("new(list) -> contains ComSci", p.contains("ComSci"));

        
        // input ที่ผิดเงื่อนไขต้องโยน exception ไม่ใช่ปล่อยผ่าน

        boolean threwDup = false;
        try {
            new BoundedStack(Arrays.asList("py", "py"));
        } catch (IllegalArgumentException e) {
            threwDup = true;
        }
        check("new(duplicates) -> throws IllegalArgumentException", threwDup);

        boolean threwSpecial = false;
        try {
            new BoundedStack(Arrays.asList("Hello", "Hello@#$$%%#"));
        } catch (IllegalArgumentException e) {
            threwSpecial = true;
        }
        check("new(list with Special characters) -> throws IllegalArgumentException", threwSpecial);

        boolean threwNullList = false;
        try {
            new BoundedStack(null);
        } catch (IllegalArgumentException e) {
            threwNullList = true;
        }
        check("new(null) -> throws IllegalArgumentException", threwNullList);

    }

    // --- Mutator: add ต้องรักษาลำดับและกันข้อความซ้ำ ---
    private static void testAdd() {
        System.out.println("\n-- Add --");

        BoundedStack s = new BoundedStack();
        check("add(Hello) -> returns true", s.push("Bohemian Rhapsody"));
        check("add(Hello) -> size 1", s.size() == 1);
        check("add(Hello) -> found by contains", s.contains("Bohemian Rhapsody"));

        // ข้อความซ้ำคืน false เฉย
        check("add duplicate -> returns false", !s.push("Bohemian Rhapsody"));
        check("failed add leaves size unchanged", s.size() == 1);

        // input ที่ผิดเงื่อนไขต้องโยน exception
        boolean threwEmpty = false;
        try {
            s.push("");
        } catch (IllegalArgumentException e) {
            threwEmpty = true;
        }
        check("add(empty string) -> throws IllegalArgumentException", threwEmpty);

        boolean threwNull = false;
        try {
            s.push(null);
        } catch (IllegalArgumentException e) {
            threwNull = true;
        }
        check("add(null) -> throws IllegalArgumentException", threwNull);

        boolean threwSpecial = false;
        try {
            s.push("ABC@&&");
        } catch (IllegalArgumentException e) {
            threwSpecial = true;
        }
        check("add(Special characters) -> throws IllegalArgumentException", threwSpecial);

        // boundary: เติมจนเต็มพอดีแล้วเติมเพิ่ม
        BoundedStack full = new BoundedStack();
        for (int i = 0; i < full.getCapacity(); i++) {
            full.push("song" + i);
        }
        check("can fill up to Capacity", full.size() == full.getCapacity());
        check("add when full -> returns false", !full.push("one more"));
    }

    // --- Mutator: remove ทั้งกรณีพบและไม่พบ ---
    private static void testRemove() {
        System.out.println("\n-- Remove --");

        BoundedStack s = new BoundedStack(Arrays.asList("A", "B", "C"));
        check("remove -> returns C", s.pop().equals("C"));
        check("remove -> size decreases", s.size() == 2);
        check("remove -> element is gone", !s.contains("C"));
        check("remove keeps the others in order",
                s.getElements().equals(Arrays.asList("A", "B")));

        // boundary: ลบจนหมด
        s.pop();
        s.pop();
        check("remove all -> empty", s.size() == 0);
        boolean threwNull = false;
        try {
            s.pop();
        } catch (IndexOutOfBoundsException e) {
            threwNull = true;
        }
        check("remove on empty list -> throws IndexOutOfBoundsException", threwNull);
    }

    // --- Observer ต้องไม่มี side effect ---
    private static void testObservers() {
        System.out.println("\n-- Observers --");

        BoundedStack s = new BoundedStack(Arrays.asList("A", "B"));
        check("contains finds an existing element", s.contains("A"));
        check("contains rejects a missing element", !s.contains("Z"));

     boolean threwNullContain= false;
        try {
            s.contains(null);
        } catch (IllegalArgumentException e) {
            threwNullContain = true;
        }
        check("contains(null) -> throws IllegalArgumentException", threwNullContain);

        boolean threwSpecial = false;
        try {
            s.contains("ABC@&&");
        } catch (IllegalArgumentException e) {
            threwSpecial = true;
        }
        check("contains(Special characters) -> throws IllegalArgumentException", threwSpecial);

        BoundedStack n = new BoundedStack();
        boolean threwNull = false;
        try {
            n.peek();
        } catch (IndexOutOfBoundsException e) {
            threwNull = true;
        }
        check("peek() = null -> throws IndexOutOfBoundsException", threwNull);
    }

    private static void testProducer() {
        System.out.println("\n-- Producer --");

        BoundedStack original = new BoundedStack(Arrays.asList("A", "B", "C", "D"));
        BoundedStack shuffled = original.shuffled();

        check("shuffled has the same size", shuffled.size() == original.size());

        List<String> a = new ArrayList<String>(original.getElements());
        List<String> b = new ArrayList<String>(shuffled.getElements());
        Collections.sort(a);
        Collections.sort(b);
        check("shuffled contains exactly the same elements", a.equals(b));

        check("shuffled does not mutate the original",
                original.getElements().equals(Arrays.asList("A", "B", "C", "D")));

        // mutate ตัวใหม่ต้องไม่กระทบตัวเดิม
        shuffled.push("E");
        check("mutating the result does not affect the original",
                original.size() == 4);

        // boundary: shuffle ลิสต์ว่างต้องไม่พัง
        BoundedStack emptyShuffled = new BoundedStack().shuffled();
        check("shuffling an empty list is safe", emptyShuffled.size() == 0);
    }

    private static void testExposure() {
        System.out.println("\n-- Exposure --");

        // ขาออก: แก้ list ที่ได้จาก songs() ต้องไม่กระทบ rep
        BoundedStack s = new BoundedStack();
        s.push("A");

        List<String> got = s.getElements();
        got.clear();
        check("clearing result of getElements() does not affect list",
                s.size() == 1);

        got = s.getElements();
        got.add("injected");
        check("adding to result of getElements() does not affect list",
                s.size() == 1 && !s.contains("injected"));

        // สองครั้งต้องเป็นคนละ object
        check("getElements() returns a fresh list each call",
                s.getElements() != s.getElements());

        // ขาเข้า: แก้ list ที่ส่งให้ constructor ต้องไม่กระทบ rep
        List<String> input = new ArrayList<String>(Arrays.asList("A", "B"));
        BoundedStack p = new BoundedStack(input);

        input.clear();
        check("clearing constructor argument does not affect list",
                p.size() == 2);

        input.add("injected");
        check("adding to constructor argument does not affect list",
                !p.contains("injected"));
    }

}
