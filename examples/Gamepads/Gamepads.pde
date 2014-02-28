import gameover.utils.*;
GameController gamepads;

PVector[] players;

void setup() {
  size(640, 480);
  gamepads = new GameController(this);
  players = new PVector[gamepads.getPadsNumber()];
  for (int i = 0; i < players.length; i++)
    players[i] = new PVector();
}

void draw() {
  background(255);
  translate(width / 2, height / 2);
  PVector joystickPosition = gamepads.getPad(0);
  fill(255, 0, 0);
  ellipse(joystickPosition.x * 30, joystickPosition.y * 30, 20, 20);

  for (int i = 0; i < players.length; i++)
    ellipse(players[i].x, players[i].y, 30, 30);
}

public void pad(int id, int pos) {
  switch(pos) {
  case GameController.PAD_LEFT: //LEFT
    players[id].x -= 10;
    break;
  case GameController.PAD_RIGHT: //RIGHT
    players[id].x += 10;
    break;
  case GameController.PAD_UP: //UP
    players[id].y -= 10;
    break;
  case GameController.PAD_DOWN: //DOWN
    players[id].y += 10;
    break;
    case GameController.PAD_UP_LEFT: //UP && LEFT
    players[id].x -= 10;
    players[id].y -= 10;
    break;
  case GameController.PAD_UP_RIGHT: //UP && RIGHT
    players[id].x += 10;
    players[id].y -= 10;
    break;
  case GameController.PAD_DOWN_LEFT: //DOWN && LEFT
    players[id].x -= 10;
    players[id].y += 10;
    break;
  case GameController.PAD_DOWN_RIGHT: //DOWN && RIGHT
    players[id].x += 10;
    players[id].y += 10;
    break;
  }
}
