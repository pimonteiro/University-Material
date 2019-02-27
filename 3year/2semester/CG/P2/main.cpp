#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#include <math.h>
#include <cstdio>

float angle_H = 0;
float angle_V = 0;

float trltx = 0;
float trltz = 0;

float s = 1;

void drawAxis();

void drawPyramid();

void changeSize(int w, int h) {

	// Prevent a divide by zero, when window is too short
	// (you cant make a window with zero width).
	if(h == 0)
		h = 1;

	// compute window's aspect ratio 
	float ratio = w * 1.0 / h;

	// Set the projection matrix as current
	glMatrixMode(GL_PROJECTION);
	// Load Identity Matrix
	glLoadIdentity();
	
	// Set the viewport to be the entire window
    glViewport(0, 0, w, h);

	// Set perspective
	gluPerspective(45.0f ,ratio, 1.0f ,1000.0f);

	// return to the model view matrix mode
	glMatrixMode(GL_MODELVIEW);
}


void renderScene(void) {
    glColor3f(1,0,1);
	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// set the camera
	glLoadIdentity();
	gluLookAt(5.0,5.0,5.0,
		      0.0,0.0,0.0,
			  0.0f,1.0f,0.0f);


// put the geometric transformations here
    drawAxis();

    glRotatef(angle_H, 0.0f, 1.0f, 0.0f);
    glRotatef(angle_V, 1.0f,0.0f,0.0f);
    glTranslatef(trltx, 0, trltz);
    glScalef(s,s,s);


// put drawing instructions here

    drawPyramid();

	// End of frame
	glutSwapBuffers();
}

void drawPyramid() {
    glBegin(GL_TRIANGLES);
    glColor3f(1,1,1);
    glVertex3f(-1,0,-1);
    glVertex3f(1,0,1);
    glVertex3f(-1,0,1);

    glVertex3f(1,0,1);
    glVertex3f(-1,0,-1);
    glVertex3f(1,0,-1);

    //Triangulo lateral 1
    glColor3f(1,1,0);
    glVertex3f(0,2,0);
    glVertex3f(-1,0,1);
    glVertex3f(1,0,1);

    //Triangulo lateral 2
    glColor3f(1,0.5,0);
    glVertex3f(0,2,0);
    glVertex3f(1,0,1);
    glVertex3f(1,0,-1);

    //Triangulo lateral 3
    glColor3f(0.5,0.5,0.3);
    glVertex3f(0,2,0);
    glVertex3f(1,0,-1);
    glVertex3f(-1,0,-1);

    //Triangulo lateral 4
    glColor3f(0.1,0.7,0.2);
    glVertex3f(0,2,0);
    glVertex3f(-1,0,-1);
    glVertex3f(-1,0,1);

    glEnd();
}

void drawAxis() {
    glBegin(GL_LINES);

    //X Axis
    glColor3f(1,0,0);
    glVertex3f(0,0,0);
    glVertex3f(255,0,0);
    glVertex3f(0 ,0,0);
    glVertex3f(-255,0,0);

    //Y Axis
    glColor3f(0,1,0);
    glVertex3f(0,0,0);
    glVertex3f(0,255,0);
    glVertex3f(0,0,0);
    glVertex3f(0,-255,0);
    //Z Axis
    glColor3f(0,0,1);
    glVertex3f(0,0,0);
    glVertex3f(0,0,255);
    glVertex3f(0,0,0);
    glVertex3f(0,0,-255);

    glEnd();
}


// write function to process keyboard events
void special_key_handling(int key_code, int x, int y){
    if(key_code == GLUT_KEY_RIGHT){
        angle_H+= 2;
        glutPostRedisplay();
    }
    if(key_code == GLUT_KEY_LEFT){
        angle_H-= 2;
        glutPostRedisplay();
    }
    if(key_code == GLUT_KEY_DOWN){
        angle_V-= 2;
        glutPostRedisplay();
    }
    if(key_code == GLUT_KEY_UP){
        angle_V+= 2;
        glutPostRedisplay();
    }
    if(key_code == GLUT_KEY_F1){
        if(s <= 3) {
            s += 0.2;
            glutPostRedisplay();
        }
    }
    if(key_code == GLUT_KEY_F2) {
        if (s > 0.2) {
            s -= 0.2;
            glutPostRedisplay();
        }
    }
}

void transformation(unsigned char key, int x, int y){
    if(key == 'd'){
        trltx++;
    }
    if(key == 'a'){
        trltx--;
    }
    if(key == 'w'){
        trltz--;
    }
    if(key == 's'){
        trltz++;
    }
    glutPostRedisplay();
}


int main(int argc, char **argv) {

// init GLUT and the window
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH|GLUT_DOUBLE|GLUT_RGBA);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(800,800);
	glutCreateWindow("CG@DI-UM");
		
// Required callback registry 
	glutDisplayFunc(renderScene);
	glutReshapeFunc(changeSize);

	
// put here the registration of the keyboard callbacks
    glutSpecialFunc(special_key_handling);
    glutKeyboardFunc(transformation);

//  OpenGL settings
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);

	
// enter GLUT's main cycle
	glutMainLoop();
	
	return 1;
}