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

package cascadia.scoring;

import cascadia.PlayerMap;

/**
 * Implements calculate score method.
 */
public abstract class ScoreToken extends Scoring {
    public static int calculateScore(PlayerMap map, Scorable option) {
        return option.score(map);
    }
}
