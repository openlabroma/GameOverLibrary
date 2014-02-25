import gameover.utils.*;

Toolbox tb;

void setup() {
  size(400,400);
  tb = new Toolbox(this);
  tb.addButton("meow", "https://github.global.ssl.fastly.net/images/spinners/octocat-spinner-32.gif", 40, 40);
}

void meow(Toolbox.Button button) {
  println("MEOW");
  exit();
}

void draw() {
  background(255);
}