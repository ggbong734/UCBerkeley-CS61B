
public class IntList {
	public int first;
	public IntList rest;

	public IntList(int f, IntList r) {
		first = f;
		rest = r;
	}

	/** Return the size of the list using... recursion! */
	public int size() {
		if (rest == null) {
			return 1;
		}
		return 1 + this.rest.size();
	}

	/** Return the size of the list using no recursion! */
	public int iterativeSize() {
		IntList p = this;
		int totalSize = 0;
		while (p != null) {
			totalSize += 1;
			p = p.rest;
		}
		return totalSize;
	}

	/** Returns the ith item of this IntList. */
	public int get(int i) {
		if (i == 0) {
			return first;
		}
		return rest.get(i - 1);
	}

    /** Returns an IntList identical to L, but with
      * each element incremented by x. L is not allowed
      * to change. */
    public static IntList incrList(IntList L, int x) {
        IntList Q = new IntList(L.first, L.rest);
        if(Q.rest == null){
        	Q.first += x;
        	return Q;
        } else {
        	Q.first += x;
        	Q.rest = incrList(Q.rest, x);
        	return 	Q;
        }
    }


    /** Returns an IntList identical to L, but with
      * each element incremented by x. Not allowed to use
      * the 'new' keyword. */
    public static IntList dincrList(IntList L, int x) {
        while(L != null){
			L.first += x;
			L = L.rest;
		} 
        return L;
    }

    /** add an element (int x) to the first spot of the list*/
    public IntList addFirst(int x){
    	IntList p = new IntList(first, rest);
    	first = x;
    	rest = p;
    }

	public static void main(String[] args) {
        IntList L = new IntList(5, null);
        L.rest = new IntList(7, null);
        L.rest.rest = new IntList(9, null);

        L.addFirst(4);
        //System.out.println(L.size());
        //System.out.println(L.iterativeSize());

        System.out.println(L.get(0));
        System.out.println(L.get(1));
        //System.out.println(incrList(L, 3));
        //System.out.println(dincrList(L, 3));  
	}
} 