/*
	COMP20050 Group 12
	Eoin Creavin – Student ID: 21390601
	eoin.creavin@ucdconnect.ie
	GitHub ID: eoin-cr

	Mynah Bhattacharyya – Student ID: 21201085
	malhar.bhattacharyya@ucdconnect.ie
	GitHub ID: mynah-bird

	Ben McDowell – Student ID: 21495144
	ben.mcdowell@ucdconnect.ie
	GitHub ID: Benmc1
 */

package cascadia;

import cascadia.scoring.ScoringSalmon;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import static org.junit.Assert.*;

public class ScoringSalmonTest {
    private PlayerMap map;
    
    @Before
    public void generateMap() {
        map = new PlayerMap();
        map.clearTileBoard();
    }

    protected static HabitatTile newTile(WildlifeToken token) {
        HabitatTile tile = new HabitatTile(HabitatTile.Habitat.Prairie, HabitatTile.Habitat.Prairie, 1);
        tile.setTokenForTesting(token);
        return tile;
    }

    // we can use this as the scoring for salmon works the same for all the options,
    // there's just different numbers for runs
    private void generalValidRunTests(List<Integer> expectedScores, ScoringSalmon.Option option) {
        map.addTileToMap(newTile(WildlifeToken.Salmon), 8, 8);

        // test whether the values for runs of 1, 2, and 3 are correct
        assertEquals(ScoringSalmon.calculateScore(map, option), (int) expectedScores.get(0));

        map.addTileToMap(newTile(WildlifeToken.Salmon), 8, 9);
        assertEquals(ScoringSalmon.calculateScore(map, option), (int) expectedScores.get(1));

        map.addTileToMap(newTile(WildlifeToken.Salmon), 8, 10);
        assertEquals(ScoringSalmon.calculateScore(map, option), (int) expectedScores.get(2));

        map.addTileToMap(newTile(WildlifeToken.Salmon), 8, 11);
        assertEquals(ScoringSalmon.calculateScore(map, option), (int) expectedScores.get(3));

        map.addTileToMap(newTile(WildlifeToken.Salmon), 9, 12);
        assertEquals(ScoringSalmon.calculateScore(map, option), (int) expectedScores.get(4));
    }

    // These runs should get invalidated as we can't have more than 2 salmon tiles touching
    // any one tile in a valid run
    private void generalInvalidRunTests(ScoringSalmon.Option option) {
        map.addTileToMap(newTile(WildlifeToken.Salmon), 8, 8);

        map.addTileToMap(newTile(WildlifeToken.Salmon), 8, 9);
        map.addTileToMap(newTile(WildlifeToken.Salmon), 8, 10);
        // test whether it returns 0 for invalid runs
        PlayerMap badMap = new PlayerMap();
        badMap.setTileBoard(PlayerMap.deepCopy(map.getTileBoardPosition()));
        badMap.addTileToMap(newTile(WildlifeToken.Salmon), 7,8);
        badMap.addTileToMap(newTile(WildlifeToken.Salmon), 9,8);
        assertEquals(ScoringSalmon.calculateScore(badMap, option), 0);

        map.addTileToMap(newTile(WildlifeToken.Salmon), 8, 11);
        badMap.setTileBoard(PlayerMap.deepCopy(map.getTileBoardPosition()));
        badMap.addTileToMap(newTile(WildlifeToken.Salmon), 7,9);
        badMap.addTileToMap(newTile(WildlifeToken.Salmon), 9,10);
        assertEquals(ScoringSalmon.calculateScore(badMap, option), 0);
    }

    @Test
    public void testSalmon1ValidRuns() {
        List<Integer> scores = new ArrayList<>();
        Collections.addAll(scores, 2,4,7,11,15);
        generalValidRunTests(scores, ScoringSalmon.Option.S1);
    }

    @Test
    public void testSalmon1InvalidRuns() {
        generalInvalidRunTests(ScoringSalmon.Option.S1);
    }

    @Test
    public void testSalmon2ValidRuns() {
        List<Integer> scores = new ArrayList<>();
        Collections.addAll(scores, 2,4,8,12,12);
        generalValidRunTests(scores, ScoringSalmon.Option.S2);
    }

    @Test
    public void testSalmon2InvalidRuns() {
        generalInvalidRunTests(ScoringSalmon.Option.S2);
    }

    @Test
    public void testSalmon3ValidRuns() {
        List<Integer> scores = new ArrayList<>();
        Collections.addAll(scores, 2,4,9,11,17);
        generalValidRunTests(scores, ScoringSalmon.Option.S3);
    }

    @Test
    public void testSalmon3InvalidRuns() {
        generalInvalidRunTests(ScoringSalmon.Option.S3);
    }
}
