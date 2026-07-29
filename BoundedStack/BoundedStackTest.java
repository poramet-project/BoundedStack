package BoundedStack;

import java.util.ArrayList;
import java.util.Arrays;

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

    }

    // --- Mutator: add ต้องรักษาลำดับและกันข้อความซ้ำ ---
    private static void testAdd() {
        System.out.println("\n-- Add --");
    }

    // --- Mutator: remove ทั้งกรณีพบและไม่พบ ---
    private static void testRemove() {
        System.out.println("\n-- Remove --");

    }

        // --- Observer ต้องไม่มี side effect ---
    private static void testObservers() {
        System.out.println("\n-- Observers --");
    }

    private static void testProducer() {
        System.out.println("\n-- Producer --");
    }

    private static void testExposure() {
        System.out.println("\n-- Exposure --");
    }

}
