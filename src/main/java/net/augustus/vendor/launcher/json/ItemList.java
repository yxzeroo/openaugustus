package net.augustus.vendor.launcher.json;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ItemList {
   private String sp = ",";
   public List items = new ArrayList();
   private static final long a = 112607017654745L;

   public void reset() {
      this.sp = ",";
      this.items.clear();
   }

   public List getItems() {
      return this.items;
   }

   public void add(long l, int n, String str) {
      if (str != null) {
         this.items.add(n, str.trim());
      }
   }

   public void addAll(long l, String str, String str2, boolean flag) {
      l = a ^ l;
      long l2 = l ^ 45249837084222L;
      this.split(str, str2, this.items, flag, l2);
   }

   public void addAll(String str, String str2) {
      this.split(str, str2, this.items);
   }

   public void addAll(String str) {
      this.split(str, this.sp, this.items);
   }

   public ItemList() {
   }

   public String[] getArray() {
      return (String[])this.items.toArray();
   }

   public void addAll2(ItemList itemList) {
      this.items.addAll(itemList.items);
   }

   @Override
   public String toString() {
      return this.toString(this.sp);
   }

   public ItemList(String str, long l, String str2, boolean flag) {
       super();
       l = a ^ l;
       long l2 = l ^ 113839271166344L;
      this.sp = ",";
      this.items = new ArrayList();
      this.split(str, str2, this.items, flag, l2);
   }

   public void setSp(String str) {
      this.sp = str;
   }

   public void clear() {
      this.items.clear();
   }

   public ItemList(String str) {
       super();
      this.sp = ",";
      this.items = new ArrayList();
      this.split(str, this.sp, this.items);
   }

   public String get(int n) {
      return (String)this.items.get(n);
   }

   public void add(long l, String str) {
      if (str != null) {
         this.items.add(str.trim());
      }
   }

   public void split(String str, String str2, List list, boolean flag, long l) {
      l = a ^ l;
      if (str != null && str2 != null) {
         if (flag) {
            StringTokenizer stringTokenizer = new StringTokenizer(str, str2);

            while (true) {
               if (stringTokenizer.hasMoreTokens()) {
                  list.add(stringTokenizer.nextToken().trim());
               } else if (l > 0L) {
                  break;
               }

               if (l > 0L) {
                  ;
               }
            }
         } else {
            this.split(str, str2, list);
         }
      }
   }

   public String toString(String str) {
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < this.items.size(); i++) {
         if (i == 0) {
            sb.append(this.items.get(i));
         } else {
            sb.append(str);
            sb.append(this.items.get(i));
         }
      }

      return sb.toString();
   }

   public void split(String str, String str2, List list) {
      if (str != null && str2 != null) {
         int n = 0;
         int n2 = 0;

         do {
            n2 = n;
            n = str.indexOf(str2, n);
            if (n == -1) {
               break;
            }

            list.add(str.substring(n, n).trim());
            n += str2.length();
         } while (n != -1);

         list.add(str.substring(n2).trim());
      }
   }

   public int size() {
      return this.items.size();
   }

   public ItemList(String str, String str2) {
       super();
      this.sp = ",";
      this.items = new ArrayList();
      this.sp = str;
      this.split(str, str2, this.items);
   }
}
