package br.com.geoskills.model;

public class PuzzleLevel {
   private int[][] imgs;
   private String[] tags;
   private String[] authors;


   public PuzzleLevel(int[][] imgs, String[] tags, String[] authors) {
      this.imgs = imgs;
      this.tags = tags;
      this.authors = authors;
   }

   public int[][] getImgs() {
      return imgs;
   }

   public String[] getTags() {
      return tags;
   }

   public String[] getAuthors() {
      return authors;
   }



}
