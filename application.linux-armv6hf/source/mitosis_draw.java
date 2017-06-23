import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class mitosis_draw extends PApplet {

ArrayList<Cell> cells;
float maxR = 10;
float maxCells = 1000;
boolean eraser;
int bkg = 255;
int fg = 0;
boolean ui = true;
PFont font;

public void setup(){
  
  font = createFont("OratorStd.otf", 12);
  textFont(font);
  cells = new ArrayList<Cell>();
  eraser = false;
  background(bkg);
}

public void draw(){
  for(int i = 0; i < cells.size(); i++){
    cells.get(i).update().render();
  }
  if(cells.size() > maxCells){
    for(int i = 0; i < min(cells.size()-maxCells, maxCells); i++){
      cells.remove(0);
    }
  }
  if(mousePressed){
    startColony(eraser);
  }
  renderUI();
}

public void renderUI(){
  noStroke();
  fill(bkg, 200);
  rect(0, 0, 200, 140);
  fill(fg);
  String command[] = {
    "P: pencil",
    "E: eraser",
    "S: save",
    "B: toggle background",
    "M: murder",
    "ESC: quit"
  };
  int ypos = 25;
  for(String c: command){
    text(c, 20, ypos);
    ypos += 20;
  }
}

public void startColony(boolean eraser){
  float r, g, b;
  if(eraser == true){
    r=255; g=255; b=255;
  } else {
    r=0; g=0; b=0;
  }
  int c = color(r, g, b, 128);
  PVector pos = new PVector(mouseX, mouseY);
  cells.add(new Cell(pos, 9, c, cells));
}

public void murder(){
  cells.clear();
}

public void toggleBkg(){
  if(bkg == 255){
    bkg = 0;
    fg = 255;
  } else {
    bkg = 255;
    fg = 0;
  }
  background(bkg);
}

public void keyPressed() {
  switch(key){
    case 'p':
    case 'P':
      eraser = false;
      break;
    case 'e':
    case 'E':
      eraser = true;
      break;
    case 's':
    case 'S':    
      save("screnshot-"+millis()+random(10)+".png");
      break;
    case 'm':
    case 'M':
      murder();
      break;
    case 'b':
    case 'B':
      toggleBkg();
      break;
  }
}
class Cell {
  ArrayList<Cell> parent;
  PVector pos;
  float r;
  int c;
  
  Cell(PVector pos, float r, int c, ArrayList<Cell> cells){
    this.pos = pos;
    this.r = r;
    this.c = c;
    this.parent = cells;
  }
  public Cell move(){
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
  public Cell grow(){
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
  public Cell mitosis(){
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
  public Cell update(){
    this.grow().move();
    return this;
  }
  public Cell render(){
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
  public int mutateColor(){
    float r = (c >> 16) & 0xFF;
    float g = (c >> 8) & 0xFF;
    float b = c & 0xFF;
    r = min(r+random(-10,10),255);
    g = min(g+random(-10,10),255);
    b = min(b+random(-10,10),255);
    return color(r,g,b);
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#030000", "--stop-color=#cccccc", "mitosis_draw" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
