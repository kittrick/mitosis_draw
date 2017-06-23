ArrayList<Cell> cells;
float maxR = 10;
float maxCells = 1000;
boolean eraser;
int bkg = 255;
int fg = 0;
boolean ui = true;
PFont font;

void setup(){
  fullScreen();
  font = createFont("OratorStd.otf", 12);
  textFont(font);
  cells = new ArrayList<Cell>();
  eraser = false;
  background(bkg);
}

void draw(){
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

void renderUI(){
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

void startColony(boolean eraser){
  float r, g, b;
  if(eraser == true){
    r=255; g=255; b=255;
  } else {
    r=0; g=0; b=0;
  }
  color c = color(r, g, b, 128);
  PVector pos = new PVector(mouseX, mouseY);
  cells.add(new Cell(pos, 9, c, cells));
}

void murder(){
  cells.clear();
}

void toggleBkg(){
  if(bkg == 255){
    bkg = 0;
    fg = 255;
  } else {
    bkg = 255;
    fg = 0;
  }
  background(bkg);
}

void keyPressed() {
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