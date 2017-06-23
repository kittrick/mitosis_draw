class Cell {
  ArrayList<Cell> parent;
  PVector pos;
  float r;
  color c;
  
  Cell(PVector pos, float r, color c, ArrayList<Cell> cells){
    this.pos = pos;
    this.r = r;
    this.c = c;
    this.parent = cells;
  }
  Cell move(){
    this.pos.add(PVector.random2D());
    if(
      (this.pos.x > width)||
      (this.pos.x < 0)||
      (this.pos.y > height)||
      (this.pos.y < 0)
    ){
      parent.remove(this);
    }
    return this;
  }
  Cell grow(){
    float grow = random(-1,1);
    this.r += grow;
    if(r < 0){
      parent.remove(this);
    }
    if(r >= maxR){
      this.mitosis();
    }
    return this; 
  }
  Cell mitosis(){
    PVector random = PVector.random2D();
    random.normalize();
    random.mult(this.r);
    PVector aPos = PVector.add(this.pos, random);
    PVector bPos = PVector.sub(this.pos, random);
    Cell a = new Cell(aPos, this.r/2, this.mutateColor(),this.parent);
    Cell b = new Cell(bPos, this.r/2, this.mutateColor(),this.parent);
    this.parent.add(a);
    this.parent.add(b);
    this.parent.remove(0);
    return this; 
  }
  Cell update(){
    this.grow().move();
    return this;
  }
  Cell render(){
    noStroke();
    fill(this.c);
    ellipse(this.pos.x, this.pos.y, this.r, this.r);
    //stroke(0, 10);
    //for(int i = 0; i < parent.size(); i++){
    //  Cell sibling = parent.get(i);
    //  line(this.pos.x, this.pos.y, sibling.pos.x, sibling.pos.y);
    //}
    return this;
  }
  color mutateColor(){
    float r = (c >> 16) & 0xFF;
    float g = (c >> 8) & 0xFF;
    float b = c & 0xFF;
    r = min(r+random(-10,10),255);
    g = min(g+random(-10,10),255);
    b = min(b+random(-10,10),255);
    return color(r,g,b);
  }
}