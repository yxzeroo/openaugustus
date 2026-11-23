package net.augustus.vendor.launcher.json;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JSONParser {
   public static final int S_IN_ERROR = -1;
   public static final int S_IN_FINISHED_VALUE = 1;
   public static final int S_INIT = 0;
   public static final int S_IN_OBJECT = 0;
   public static final int S_IN_ARRAY = 0;
   public static final int S_PASSED_PAIR_KEY = 0;
   public static final int S_IN_PAIR_VALUE = 0;
   public static final int S_END = 0;
   private LinkedList handlerStatusStack;
   private Yylex lexer;
   private int status;
   private Yytoken token;
   private static final long a = 60336046026743L;

   public void reset() {
      this.token = null;
      this.status = 0;
      this.handlerStatusStack = null;
   }

   public Object parse2(String str, int n, short s, char ch) throws JsonParseException {
      long l2 = (((long)n << 32 | (long)s << 48 >>> 32 | (long)ch << 48 >>> 48) ^ a) ^ 100713264008964L;
      return this.parse4(str, (ContainerFactory)null, l2);
   }

   public void parse(Reader reader, ContentHandler contentHandler, boolean flag, long l) throws IOException, JsonParseException {
      l = a ^ l;
      long l2 = l ^ 17225097225919L;
      int n = (int)((l ^ 17225097225919L) >>> 32);
      int n2 = (int)((l ^ 17225097225919L) << 32 >>> 48);
      int n3 = (int)(l2 << 48 >>> 48);
      long l3 = l ^ 101311778410027L;
      if (!flag) {
         this.reset(reader);
         long l4;
         int n4 = (l4 = l - 0L) == 0L ? 0 : (l4 < 0L ? -1 : 1);
         this.handlerStatusStack = new LinkedList();
      } else if (this.handlerStatusStack == null) {
         flag = false;
         this.reset(reader);
         this.handlerStatusStack = new LinkedList();
      }

      LinkedList handlerStatusStack2 = this.handlerStatusStack;

      try {
         do {
            exitBlock:
            switch (this.status) {
               case -1:
                  throw new JsonParseException(this.getPosition(), 1, this.token);
               case 0:
                  contentHandler.startJSON();
                  this.nextToken(n, (char)n2, (short)n3);
                  long l5;
                  int n5 = (l5 = l - 0L) == 0L ? 0 : (l5 < 0L ? -1 : 1);
                  switch (this.token.type) {
                     case 0:
                        this.status = 1;
                        handlerStatusStack2.addFirst(new Integer(this.status));
                        if (!contentHandler.primitive(this.token.value)) {
                           return;
                        }
                        break exitBlock;
                     case 1:
                        this.status = 0;
                        handlerStatusStack2.addFirst(new Integer(this.status));
                        if (!contentHandler.startObject()) {
                           return;
                        }
                        break exitBlock;
                     case 2:
                     default:
                        this.status = -1;
                        long l6;
                        int n6 = (l6 = l - 0L) == 0L ? 0 : (l6 < 0L ? -1 : 1);
                        break exitBlock;
                     case 3:
                        this.status = 0;
                        handlerStatusStack2.addFirst(new Integer(this.status));
                        if (!contentHandler.startArray()) {
                           return;
                        }
                        break exitBlock;
                  }
               case 1:
                  this.nextToken(n, (char)n2, (short)n3);
                  long l7;
                  int n7 = (l7 = l - 0L) == 0L ? 0 : (l7 < 0L ? -1 : 1);
                  if (this.token.type == -1) {
                     contentHandler.endJSON();
                     this.status = 0;
                     return;
                  }

                  this.status = -1;
                  throw new JsonParseException(this.getPosition(), 1, this.token);
               case 2:
                  this.nextToken(n, (char)n2, (short)n3);
                  long l8;
                  int n8 = (l8 = l - 0L) == 0L ? 0 : (l8 < 0L ? -1 : 1);
                  switch (this.token.type) {
                     case 0:
                        long l9;
                        int n9 = (l9 = l - 0L) == 0L ? 0 : (l9 < 0L ? -1 : 1);
                        long l10;
                        int n10 = (l10 = l - 0L) == 0L ? 0 : (l10 < 0L ? -1 : 1);
                        if (this.token.value instanceof String) {
                           String value = (String)this.token.value;
                           this.status = 0;
                           handlerStatusStack2.addFirst(new Integer(this.status));
                           if (!contentHandler.startObjectEntry(value)) {
                              return;
                           }
                        } else {
                           this.status = -1;
                        }
                        break exitBlock;
                     case 2:
                        if (handlerStatusStack2.size() > 1) {
                           handlerStatusStack2.removeFirst();
                           int n11 = this.peekStatus(handlerStatusStack2, l3);
                           long l11;
                           int n12 = (l11 = l - 0L) == 0L ? 0 : (l11 < 0L ? -1 : 1);
                           this.status = n11;
                        } else {
                           this.status = 1;
                        }

                        if (!contentHandler.endObject()) {
                           return;
                        }
                     case 5:
                        break exitBlock;
                     default:
                        this.status = -1;
                        long l12;
                        n8 = (l12 = l - 0L) == 0L ? 0 : (l12 < 0L ? -1 : 1);
                        break exitBlock;
                  }
               case 3:
                  this.nextToken(n, (char)n2, (short)n3);
                  long l13;
                  int n13 = (l13 = l - 0L) == 0L ? 0 : (l13 < 0L ? -1 : 1);
                  switch (this.token.type) {
                     case 0:
                        if (!contentHandler.primitive(this.token.value)) {
                           return;
                        }
                        break exitBlock;
                     case 1:
                        this.status = 0;
                        handlerStatusStack2.addFirst(new Integer(this.status));
                        if (!contentHandler.startObject()) {
                           return;
                        }
                        break exitBlock;
                     case 2:
                     default:
                        this.status = -1;
                        break exitBlock;
                     case 3:
                        this.status = 0;
                        handlerStatusStack2.addFirst(new Integer(this.status));
                        if (!contentHandler.startArray()) {
                           return;
                        }
                        break exitBlock;
                     case 4:
                        if (handlerStatusStack2.size() > 1) {
                           handlerStatusStack2.removeFirst();
                           int n14 = this.peekStatus(handlerStatusStack2, l3);
                           long l14;
                           int n15 = (l14 = l - 0L) == 0L ? 0 : (l14 < 0L ? -1 : 1);
                           this.status = n14;
                        } else {
                           this.status = 1;
                        }

                        if (!contentHandler.endArray()) {
                           return;
                        }
                     case 5:
                        break exitBlock;
                  }
               case 4:
                  this.nextToken(n, (char)n2, (short)n3);
                  long l15;
                  int n16 = (l15 = l - 0L) == 0L ? 0 : (l15 < 0L ? -1 : 1);
                  switch (this.token.type) {
                     case 0:
                        handlerStatusStack2.removeFirst();
                        this.status = this.peekStatus(handlerStatusStack2, l3);
                        boolean flag2 = contentHandler.primitive(this.token.value);
                        long l16;
                        int n17 = (l16 = l - 0L) == 0L ? 0 : (l16 < 0L ? -1 : 1);
                        if (!flag2) {
                           return;
                        }

                        if (!contentHandler.endObjectEntry()) {
                           return;
                        }
                        break exitBlock;
                     case 1:
                        handlerStatusStack2.removeFirst();
                        handlerStatusStack2.addFirst(new Integer(5));
                        this.status = 0;
                        handlerStatusStack2.addFirst(new Integer(this.status));
                        if (!contentHandler.startObject()) {
                           return;
                        }
                        break exitBlock;
                     case 2:
                     case 4:
                     case 5:
                     default:
                        this.status = -1;
                        long l17;
                        int n18 = (l17 = l - 0L) == 0L ? 0 : (l17 < 0L ? -1 : 1);
                        break exitBlock;
                     case 3:
                        handlerStatusStack2.removeFirst();
                        handlerStatusStack2.addFirst(new Integer(5));
                        this.status = 0;
                        handlerStatusStack2.addFirst(new Integer(this.status));
                        if (!contentHandler.startArray()) {
                           return;
                        }
                        break exitBlock;
                     case 6:
                        long l18;
                        int n19 = (l18 = l - 0L) == 0L ? 0 : (l18 < 0L ? -1 : 1);
                        break exitBlock;
                  }
               case 5:
                  handlerStatusStack2.removeFirst();
                  this.status = this.peekStatus(handlerStatusStack2, l3);
                  if (!contentHandler.endObjectEntry()) {
                     return;
                  }
                  break;
               case 6:
                  return;
            }

            if (this.status == -1) {
               throw new JsonParseException(this.getPosition(), 1, this.token);
            }
         } while (this.token.type != -1);
      } catch (IOException e) {
         this.status = -1;
         throw e;
      } catch (JsonParseException e2) {
         this.status = -1;
         throw e2;
      } catch (RuntimeException e3) {
         this.status = -1;
         throw e3;
      } catch (Error e4) {
         this.status = -1;
         throw e4;
      }

      long l19;
      int n20 = (l19 = l - 0L) == 0L ? 0 : (l19 < 0L ? -1 : 1);
      this.status = -1;
      throw new JsonParseException(this.getPosition(), 1, this.token);
   }

   public Object parse(Reader reader, long l) throws IOException, JsonParseException {
      l = a ^ l;
      return this.parse3(l ^ 88682274560119L, reader, (ContainerFactory)null);
   }

   private List createArrayContainer(ContainerFactory containerFactory) {
      if (containerFactory == null) {
         return new JSONArray();
      } else {
         List list = containerFactory.creatArrayContainer();
         return (List)(list == null ? new JSONArray() : list);
      }
   }

   public Object parse3(long l, Reader reader, ContainerFactory containerFactory) throws IOException, JsonParseException {
      l = a ^ l;
      long l2 = l ^ 21554492692748L;
      int n = (int)((l ^ 21554492692748L) >>> 32);
      int n2 = (int)((l ^ 21554492692748L) << 32 >>> 48);
      int n3 = (int)(l2 << 48 >>> 48);
      l2 = l ^ 63334836585455L;
      long l3 = l ^ 70456534905240L;
      this.reset(reader);
      LinkedList list = new LinkedList();
      LinkedList list2 = new LinkedList();

      try {
         do {
            this.nextToken(n, (char)n2, (short)n3);
            exitBlock:
            switch (this.status) {
               case -1:
                  throw new JsonParseException(this.getPosition(), 1, this.token);
               case 0:
                  switch (this.token.type) {
                     case 0:
                        long l4;
                        int n4 = (l4 = l - 0L) == 0L ? 0 : (l4 < 0L ? -1 : 1);
                        this.status = 1;
                        list.addFirst(new Integer(this.status));
                        list2.addFirst(this.token.value);
                        break exitBlock;
                     case 1:
                        this.status = 0;
                        list.addFirst(new Integer(this.status));
                        list2.addFirst(this.createObjectContainer(containerFactory));
                        break exitBlock;
                     case 2:
                     default:
                        this.status = -1;
                        break exitBlock;
                     case 3:
                        long l5;
                        int n5 = (l5 = l - 0L) == 0L ? 0 : (l5 < 0L ? -1 : 1);
                        this.status = 0;
                        list.addFirst(new Integer(this.status));
                        list2.addFirst(this.createArrayContainer(containerFactory));
                        break exitBlock;
                  }
               case 1:
                  if (this.token.type == -1) {
                     return list2.removeFirst();
                  }

                  throw new JsonParseException(this.getPosition(), 1, this.token);
               case 2:
                  switch (this.token.type) {
                     case 0:
                        long l6;
                        int n6 = (l6 = l - 0L) == 0L ? 0 : (l6 < 0L ? -1 : 1);
                        long l7;
                        int n7 = (l7 = l - 0L) == 0L ? 0 : (l7 < 0L ? -1 : 1);
                        if (this.token.value instanceof String) {
                           list2.addFirst((String)this.token.value);
                           long l8;
                           int n8 = (l8 = l - 0L) == 0L ? 0 : (l8 < 0L ? -1 : 1);
                           this.status = 0;
                           list.addFirst(new Integer(this.status));
                        } else {
                           this.status = -1;
                        }
                        break exitBlock;
                     case 2:
                        if (list2.size() > 1) {
                           list.removeFirst();
                           list2.removeFirst();
                           long l9;
                           int n9 = (l9 = l - 0L) == 0L ? 0 : (l9 < 0L ? -1 : 1);
                           this.status = this.peekStatus(list, l3);
                        } else {
                           long l10;
                           int n10 = (l10 = l - 0L) == 0L ? 0 : (l10 < 0L ? -1 : 1);
                           this.status = 1;
                        }
                     case 5:
                        break exitBlock;
                     default:
                        this.status = -1;
                        break exitBlock;
                  }
               case 3:
                  switch (this.token.type) {
                     case 0:
                        ((List)list2.getFirst()).add(this.token.value);
                        long l11;
                        int n11 = (l11 = l - 0L) == 0L ? 0 : (l11 < 0L ? -1 : 1);
                        break exitBlock;
                     case 1:
                        List first2 = (List)list2.getFirst();
                        Map map = this.createObjectContainer(containerFactory);
                        first2.add(map);
                        long l12;
                        int n12 = (l12 = l - 0L) == 0L ? 0 : (l12 < 0L ? -1 : 1);
                        this.status = 0;
                        list.addFirst(new Integer(this.status));
                        list2.addFirst(map);
                        break exitBlock;
                     case 2:
                     default:
                        this.status = -1;
                        break exitBlock;
                     case 3:
                        List first3 = (List)list2.getFirst();
                        List list3 = this.createArrayContainer(containerFactory);
                        first3.add(list3);
                        long l13;
                        int n13 = (l13 = l - 0L) == 0L ? 0 : (l13 < 0L ? -1 : 1);
                        this.status = 0;
                        list.addFirst(new Integer(this.status));
                        list2.addFirst(list3);
                        break exitBlock;
                     case 4:
                        if (list2.size() > 1) {
                           list.removeFirst();
                           list2.removeFirst();
                           long l14;
                           int n14 = (l14 = l - 0L) == 0L ? 0 : (l14 < 0L ? -1 : 1);
                           this.status = this.peekStatus(list, l3);
                        } else {
                           long l15;
                           int n15 = (l15 = l - 0L) == 0L ? 0 : (l15 < 0L ? -1 : 1);
                           this.status = 1;
                        }
                        break exitBlock;
                     case 5:
                        long l16;
                        int n16 = (l16 = l - 0L) == 0L ? 0 : (l16 < 0L ? -1 : 1);
                        break exitBlock;
                  }
               case 4:
                  switch (this.token.type) {
                     case 0:
                        list.removeFirst();
                        String str = (String)list2.removeFirst();
                        ((Map)list2.getFirst()).put(str, this.token.value);
                        long l17;
                        int n17 = (l17 = l - 0L) == 0L ? 0 : (l17 < 0L ? -1 : 1);
                        this.status = this.peekStatus(list, l3);
                        break;
                     case 1:
                        list.removeFirst();
                        String str2 = (String)list2.removeFirst();
                        Map first5 = (Map)list2.getFirst();
                        Map map2 = this.createObjectContainer(containerFactory);
                        first5.put(str2, map2);
                        long l18;
                        int n18 = (l18 = l - 0L) == 0L ? 0 : (l18 < 0L ? -1 : 1);
                        this.status = 0;
                        list.addFirst(new Integer(this.status));
                        list2.addFirst(map2);
                        break;
                     case 2:
                     case 4:
                     case 5:
                     default:
                        this.status = -1;
                        break;
                     case 3:
                        list.removeFirst();
                        String str3 = (String)list2.removeFirst();
                        Map first6 = (Map)list2.getFirst();
                        List list4 = this.createArrayContainer(containerFactory);
                        first6.put(str3, list4);
                        long l19;
                        int n19 = (l19 = l - 0L) == 0L ? 0 : (l19 < 0L ? -1 : 1);
                        this.status = 0;
                        list.addFirst(new Integer(this.status));
                        list2.addFirst(list4);
                        break;
                     case 6:
                        long l20;
                        int n20 = (l20 = l - 0L) == 0L ? 0 : (l20 < 0L ? -1 : 1);
                  }
            }

            if (this.status == -1) {
               throw new JsonParseException(this.getPosition(), 1, this.token);
            }
         } while (this.token.type != -1);
      } catch (IOException e) {
         throw e;
      }

      long l21;
      int n21 = (l21 = l - 0L) == 0L ? 0 : (l21 < 0L ? -1 : 1);
      throw new JsonParseException(this.getPosition(), 1, this.token);
   }

   public void parse2(String str, ContentHandler contentHandler, long l) throws JsonParseException {
      l = a ^ l;
      this.parse3(l ^ 53870302312720L, str, contentHandler, false);
   }

   private void nextToken(int n, char ch, short s) throws JsonParseException, IOException {
      long l2 = (((long)n << 32 | (long)ch << 48 >>> 32 | (long)s << 48 >>> 48) ^ a) ^ 67659315671772L;
      this.token = this.lexer.yylex(l2);
      if (this.token == null) {
         this.token = new Yytoken(-1, null);
      }
   }

   public void reset(Reader reader) {
      this.lexer.yyreset(reader);
      this.reset();
   }

   public JSONParser() {
       super();
      this.lexer = new Yylex((Reader)null);
      this.token = null;
      this.status = 0;
   }

   public int getPosition() {
      return this.lexer.getYychar();
   }

   private Map createObjectContainer(ContainerFactory containerFactory) {
      if (containerFactory == null) {
         return new JSONObject();
      } else {
         Map map = containerFactory.createObjectContainer();
         return (Map)(map == null ? new JSONObject() : map);
      }
   }

   private int peekStatus(LinkedList list, long l) {
      l = a ^ l;
      int size = list.size();
      long l2;
      int n = (l2 = l - 0L) == 0L ? 0 : (l2 < 0L ? -1 : 1);
      if (size == 0) {
         return -1;
      } else {
         return (Integer)list.getFirst();
      }
   }

   public void parse(Reader reader, ContentHandler contentHandler, long l) throws IOException, JsonParseException {
      l = a ^ l;
      this.parse(reader, contentHandler, false, l ^ 91064140169215L);
   }

   public Object parse4(String str, ContainerFactory containerFactory, long l) throws JsonParseException {
      l = a ^ l;
      long l2 = l ^ 111474575221311L;
      StringReader stringReader = new StringReader(str);

      try {
         return this.parse3(l2, stringReader, containerFactory);
      } catch (IOException e) {
         throw new JsonParseException(-1, 2, e);
      }
   }

   public void parse3(long l, String str, ContentHandler contentHandler, boolean flag) throws JsonParseException {
      l = a ^ l;
      long l2 = l ^ 135895755627627L;
      StringReader stringReader = new StringReader(str);

      try {
         this.parse(stringReader, contentHandler, flag, l2);
      } catch (IOException e) {
         throw new JsonParseException(-1, 2, e);
      }
   }

}
