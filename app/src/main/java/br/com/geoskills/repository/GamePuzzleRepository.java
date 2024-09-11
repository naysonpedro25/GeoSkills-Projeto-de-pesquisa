package br.com.geoskills.repository;



import java.util.Arrays;
import java.util.List;

import br.com.geoskills.R;
import br.com.geoskills.model.PuzzleLevel;

public class GamePuzzleRepository {
   public static final int LEVEL_NUM = 3;


   public static List<PuzzleLevel> getAllLevels() {
      return Arrays.asList(new PuzzleLevel(new int[][]{
                      {R.drawable.mercator_1, R.drawable.mercator_2, R.drawable.mercator_3},
                      {R.drawable.dyamaxion_1, R.drawable.dyamaxion_2, R.drawable.dyamaxion_3},
                      {br.com.geoskills.R.drawable.azimuthal_1, R.drawable.azimuthal_2, R.drawable.azimuthal_3},
              }, new String[]{"tag1", "tag2", "tag3"}, new String[]{"Mercator", "Dymaxion", "Azimuthal"}),
              new PuzzleLevel(new int[][]{
                      {R.drawable.cassini_1, R.drawable.cassini_2, R.drawable.cassini_3},
                      {R.drawable.cahill_keyes_1, R.drawable.cahill_keyes_2, R.drawable.cahill_keyes_3},
                      {R.drawable.stereographic_1, R.drawable.stereographic_2, R.drawable.stereographic_3},
              }, new String[]{"tag4", "tag5", "tag6"}, new String[]{"Cassini", "Cahill Keyes", "Stereographic"}),
              new PuzzleLevel(new int[][]{
                      {R.drawable.gaus_kruger_1, R.drawable.gaus_kruger_2, R.drawable.gaus_kruger_3},
                      {R.drawable.two_points_1, R.drawable.two_points_2, R.drawable.two_points_3},
                      {R.drawable.waterman_1, R.drawable.waterman_2, R.drawable.waterman_3},
              }, new String[]{"tag7", "tag8", "tag9"}, new String[]{"Gauss Kruger", "Two Points" ,"Waterman"})
      );
   }
}
