import java.util.Random;

public class RBTree implements OrderedSet {
	
	protected final static boolean RED = true;
	protected final static boolean BLACK = false;
	
///////////////////////////////////////////////////////////////////////

		protected static class Node {
			public Comparable data;
			public boolean colour;
			public Node left;
			public Node right;
			public Node parent;
			
			public Node(Comparable data, Node parent) {
				this.data = data;
				this.colour = RED;
				this.left = null;
				this.right = null;
				this.parent = parent;
			}
		
			public Node(Comparable data) {
				this(data, null);
			}
		
			public Node getUncle() {
				Node p = this.parent;
				if (p!= null){
					Node gp = p.parent;
					if (gp != null) {
						if (p == gp.left) return gp.right;
						else return gp.left;
					} else return null;
				} else return null;
			}
		
			public boolean isLeftChild() {
				return parent != null && this == parent.left;
			}
		
			public boolean isRightChild() {
				return parent != null && this == parent.right;
			}
		}
///////////////////////////////////////////////////////////////////////	

	protected Node root;
	protected boolean balanced;
	protected Node current;//returned from first() or next().
	protected int size;

///////////////////////////////////////////////////////////////////////	
		
		public RBTree(boolean balanced) {
			root = null;
			current = null;
			this.balanced = balanced;
			size = 0;
		}
		
///////////////////////////////////////////////////////////////////////
		
		public void add(Comparable data) {
			if (root == null) {
				root = new Node(data);
				size = 1;
				if (balanced) root.colour = BLACK;
			}
			else bstAdd(root, data);
		}
		
///////////////////////////////////////////////////////////////////////	
		
		protected void bstAdd(Node n, Comparable data) {
			int comp = data.compareTo(n.data);
			if (comp == 0) return;

			if (comp == 1 ) {
				if (n.right == null) {
					//System.out.println("comp is 1, n.right is null, adding: " + data);
					n.right = new Node(data, n);
					size++;
					if (balanced) resolveInbalance(n.right);
				} else 
					//System.out.println("comp is 1, n.right is not null, recursion: " + data);
					bstAdd(n.right, data); 
				
			}
			else {
				if (n.left == null) {
					//System.out.println("comp is -1, n.left is null, adding: " + data);
					n.left = new Node(data, n);
					size ++;
					if (balanced) resolveInbalance(n.left);
				} 
				else 
					//System.out.println("comp is -1, n.left is not null, recursion: " + data);
					bstAdd(n.left, data);
				
			}
			
		}
		
///////////////////////////////////////////////////////////////////////	

	protected void resolveInbalance(Node n) {
		//System.out.println("in resolve inbalance");
		if (n == root) { 
			n.colour = BLACK;
			//System.out.println("node is root returning");
			return;
		}
		
		Node u = n.getUncle();
		Node p = n.parent;
		
		if (p.colour == BLACK) {
			//System.out.println("node parent is black returning");
			return;
		}
		
		if (u != null && u.colour == RED) {
			//System.out.println("uncle exists and is red, repainting");
				p.colour = BLACK;
				u.colour = BLACK; 
				p.parent.colour = RED;
				resolveInbalance(p.parent);
				return;
		}
		else if (u == null || u.colour == BLACK) {
			if (p.isRightChild() && n.isRightChild()){
				//System.out.println("p.isright, n.isright going to RRr");
				singleLeftRotate(p);
			}
			else if (n.isLeftChild() && p.isLeftChild()){
				//System.out.println("p.isleft, n.isleft going to LLb");
				singleRightRotate(p);
			 }
			else if (p.isLeftChild() && n.isRightChild()) {
				//System.out.println("p.isleft, n.isright going to RLb");
				doubleRightRotate(p);
			} 
			else if (p.isRightChild() && n.isLeftChild()) {
				//System.out.println("p.isright, n.isleft going to RLb");
				doubleLeftRotate(p);
			} 
		}
	}

///////////////////////////////////////////////////////////////////////	

	protected void singleLeftRotate(Node n) {
		Node a = n.parent;
		Node b = n;
		Node c = n.right;

		Node gp = a.parent;
		Node two = b.left;
		
		if (two != null) {
			a.right = two;
			two.parent = a;
		} else { a.right = null; }
		
		if (gp == null) {
			b.parent = null;
			root = b;
		} else {
			b.parent = a.parent;
			if (a.parent.right == a) a.parent.right = b;
			else a.parent.left = b;
		}

		b.right = c;
		c.parent = b;
		b.left = a;
		a.parent = b;
		
		b.colour = BLACK;
		a.colour = RED;
	}

///////////////////////////////////////////////////////////////////////	

	protected void singleRightRotate(Node n) {
		Node a = n.parent;
		Node b = n;
		Node c = n.left;

		Node gp = a.parent;
		Node two = b.right;
		
		if (two != null) {
			a.left = two;
			two.parent = a;
			//System.out.println("singleRightRotate, placing b.right to a.left");
		} else { a.left = null;}
		
		
		if (gp == null) {
			b.parent = null;
			root = b;
		} else {
			b.parent = a.parent;
			if (a.parent.right == a) a.parent.right = b;
			else a.parent.left = b;
		}
		
		b.right = a;
		a.parent = b;
		b.left = c;
		c.parent = b;
		
		
		b.colour = BLACK;
		a.colour = RED;
	}
	
///////////////////////////////////////////////////////////////////////	
//RLb
	protected void doubleLeftRotate(Node n) {
		Node a = n.parent;
		//System.out.println("A: " + a.data);
		Node b = n;
		//System.out.println("B: " + b.data);
		Node c = n.left;
		//System.out.println("C: " + c.data);
		
		Node gp = a.parent;
		Node two = c.left;
		Node three = c.right;
		
		if (two != null) {
			a.right = two;
			two.parent = a;
		} else { 
			a.right = null; 
			}
		
		if (three != null) {
			b.left = three;
			three.parent = b;
		} else { 
			b.left = null;
			}
		
	// TO-DO: attach the new subtree root, "b", to the rest of the
		// tree. That is, attach "b" as either the left or right child of
		// "gp", depending on where "a" used to be when we started. If 
		// "gp" is null, then set b.parent to null, and make b the new root
		// of the entire tree.
		// ...
		if (gp == null) {
			c.parent = null;
			root = c;
		} else {
			c.parent = a.parent;
			if (a.parent.right == a) a.parent.right = c;
			else a.parent.left = c;
		}
		
		c.right = b;
		b.parent = c;
		c.left = a;
		a.parent = c;
		
		c.colour = BLACK;
		a.colour = RED;
	}

///////////////////////////////////////////////////////////////////////	
//LLb
	protected void doubleRightRotate(Node n) {
		Node a = n.parent;
		Node b = n;
		Node c = n.right;

		Node gp = a.parent;
		Node two = c.left;
		Node three = c.right;
		
		if (two != null) {
			b.right = two;
			two.parent = b;
		} else { b.right = null; }
		
		if (three != null) {
			a.left = three;
			three.parent = a;
		} else { a.left = null;}
		
		if (gp == null) {
			c.parent = null;
			root = c;
		} else {
			c.parent = a.parent;
			if (a.parent.right == a) a.parent.right = c;
			else a.parent.left = c;
		}
		
		
		c.right = a;
		a.parent = c;
		c.left = b;
		b.parent = c;
		
		c.colour = BLACK;
		a.colour = RED;
	}

	
///////////////////////////////////////////////////////////////////////	
	
	public boolean contains(Comparable data) {
		//System.out.println("In contains, " + data);
		Node find = bstFind(root, data);
		if (find != null) return true;
		return false;
	}
	
///////////////////////////////////////////////////////////////////////	
	
	protected Node bstFind(Node n, Comparable data) {
		if (n == null) return null;
		int comp = n.data.compareTo(data);
		if (comp == 0) return n;
		else if (comp == 1) return bstFind(n.left, data);
		else return bstFind(n.right, data); 
	}

///////////////////////////////////////////////////////////////////////	
	
	public Comparable first() {
		current = leftMostDecendant(root);
		if (current != null) return current.data;
		return null;
	}
	
///////////////////////////////////////////////////////////////////////

	public Comparable next() {
		current = successor(current);
		if (current == null) return null;
		else return current.data;
	}
	
///////////////////////////////////////////////////////////////////////	
	
	public int size() { 
		return size;
	}
	
///////////////////////////////////////////////////////////////////////	
	
	protected Node leftMostDecendant(Node n) {
		if (n == null) return null;
		if(n.left == null) return n;
		return leftMostDecendant(n.left);
	}
	
///////////////////////////////////////////////////////////////////////	

	protected Node successor(Node n) {
		if (n.right != null) return leftMostDecendant(n.right);
		else {
			Node p = n.parent;
			while (p!= null && n == p.right) {
				n = p;
				p = p.parent;
			}
		return p;
		}
	}
	
///////////////////////////////////////////////////////////////////////	
	public int height() {
		return height(root);
	}
///////////////////////////////////////////////////////////////////////
	
	protected int height(Node n) {
		if (n == null) return 0;
		else return 1 + Math.max(height(n.left), height(n.right));
	}
	
///////////////////////////////////////////////////////////////////////	
	
	public int blackHeight() {
		return blackHeight(root);
	}
	
///////////////////////////////////////////////////////////////////////	
	//the count of all black nodes from any node to any leaf. 
	protected int blackHeight(Node n) {
		if (n == null) return 1;
		if (n.colour == BLACK) return 1 + blackHeight(n.left); 
		else return 0 + blackHeight(n.left);
	}
	
///////////////////////////////////////////////////////////////////////	
	
	protected boolean validateBlackHeight() {
		if (root == null) return true;
		else return blackHeight(root.left) == blackHeight(root.right);
	}
	
///////////////////////////////////////////////////////////////////////	
	
	public void inorder() {
		inorder(root);
		System.out.println();
	}
	
///////////////////////////////////////////////////////////////////////
	
	protected void inorder(Node n) {
		if (n != null) {
			inorder(n.left);
			System.out.print(n.data + " ");
			inorder(n.right);
		}
	}
	
///////////////////////////////////////////////////////////////////////	

	public static void main(String[] args) {
		
/*	RBTree rb = new RBTree(true);
	
	int[] input = {29, 10, 15, 8, 40,35, 9, 13, 12,11, 1, 5, 50, 45, 20, 19};
	for (int i = 0; i < input.length; i++)
		rb.add(input[i]);
		
		System.out.println("Size: " + rb.size());
		System.out.println("First: " + rb.first());
		System.out.println("Root data: " + rb.root.data);
		System.out.println("Root colour: " + rb.root.colour);
		System.out.println("Root left colour: " + rb.root.left.colour);
		System.out.println("Root right colour: " + rb.root.right.colour);
		System.out.println("BlackHeight: " + rb.blackHeight());
		rb.inorder();
	}
}*/
	
	
	
		Random rand = new Random();
		RBTree bst = new RBTree(false);
		RBTree rb = new RBTree(true);
		boolean bstError = false;
		boolean rbError = false;

		int n = 1000;
		boolean debug = false;
		int[] input = { 20, 6, 13, 25};
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--debug")) debug = true;
			if (args[i].startsWith("--n=")) {
				String[] parts = args[i].split("=");
				n = Integer.parseInt(parts[1]);
			}
		}
		
		System.out.println("****** BEGIN INSERTION ******");
		for (int i = 0; i < n; i++) {
			int data = debug ? rand.nextInt(100) : rand.nextInt();
			if (!bstError) {
				try {
					bst.add(new Integer(data));
				} catch (Exception e) {
					System.out.println("Error inserting into BST.");
					bstError = true;
				}
			}
			
			if (!rbError) {
				try {
					rb.add(new Integer(data));
				} catch (Exception e) {
					System.out.println("Error inserting into RB Tree.");
					e.printStackTrace();
					rbError = true;
				}
			}
		}
		System.out.println("****** END INSERTION ******\n");

		if (!bstError) {
			System.out.println("****** BEGIN BST ORDER TEST ******");
			if (debug) bst.inorder();
			Comparable last = null, next = null;
			try {
				last = bst.first();
			} catch (Exception e) {
				System.out.println("ERROR! Exception when calling BST first():");
				e.printStackTrace();
				bstError = true;
			}
			while (!bstError && last != null) {
				try {
					next = bst.next();
				} catch (Exception e) {
					System.out.println("ERROR! Exception when calling BST next():");
					e.printStackTrace();
					bstError = true;
				}
				if (next != null) {
					if (next.compareTo(last) < 0) {
						System.out.println("ERROR! " + last + " came before " + next);
						bstError = true;
					} else if (next.compareTo(last) == 0) {
						System.out.println("ERROR! Duplicate entries for " + last);
						bstError = true;
					}
				}
				last = next;
			}
			System.out.println("****** END BST ORDER TEST ******");
			System.out.println("BST height: " + bst.height());
			System.out.println("BST size: " + bst.size());
			System.out.println();
		}

		if (!rbError) {
			System.out.println("****** BEGIN RB TREE ORDER TEST ******");
			if (debug) rb.inorder();
			Comparable last = null, next = null;
			try {
				last = rb.first();
			} catch (Exception e) {
				System.out.println("ERROR! Exception when calling RB first():");
				e.printStackTrace();
				rbError = true;
			}
			while (!rbError && last != null) {
				try {
					next = rb.next();
				} catch (Exception e) {
					System.out.println("ERROR! Exception when calling RB next():");
					e.printStackTrace();
					rbError = true;
				}
				if (next != null) {
					if (next.compareTo(last) < 0) {
						System.out.println("ERROR! " + last + " came before " + next);
						rbError = true;
					} else if (next.compareTo(last) == 0) {
						System.out.println("ERROR! Duplicate entries for " + last);
						rbError = true;
					}
				}
				last = next;
			}
			System.out.println("****** END RB TREE ORDER TEST ******");
			System.out.println("RB height: " + rb.height());
			System.out.println("RB size: " + rb.size());
			System.out.println("RB blackHeight: " + rb.blackHeight());
			System.out.println("Valid RB blackHeight property: " + rb.validateBlackHeight());
			System.out.println();
		}
	rb.inorder();	
	}
}
