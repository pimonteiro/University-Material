#include <stdio.h>

#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#define _USE_MATH_DEFINES
#include <math.h>
#include <time.h>

float alfa = 0.0f, beta = 0.5f, radius = 100.0f;
float camX, camY, camZ;
float rotAng = 0;

void spherical2Cartesian() {

	camX = radius * cos(beta) * sin(alfa);
	camY = radius * sin(beta);
	camZ = radius * cos(beta) * cos(alfa);
}


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

void drawTree(float x, float z) {
    float c = (1.0f * rand() / RAND_MAX) / 2;

    glPushMatrix();
    glTranslatef(x,0,z);
    glRotatef(90,-1,0,0);

    glColor3f(0.8,0.3,0.1);
    glutSolidCone(0.5,4,6,6);
   // glRotatef(-90,-1,0,0);
    glTranslatef(0,0,3);
    //glRotatef(90,-1,0,0);

    glColor3f(0,1-c,0);
    glutSolidCone(2,8,10,10);
    glPopMatrix();
}



void renderScene(void) {

	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// set the camera
	glLoadIdentity();
	gluLookAt(camX, camY, camZ,
		0.0, 0.0, 0.0,
		0.0f, 1.0f, 0.0f);

	glColor3f(0.2f, 0.8f, 0.2f);
	glBegin(GL_TRIANGLES);
		glVertex3f(100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, 100.0f);

		glVertex3f(100.0f, 0, -100.0f);
		glVertex3f(-100.0f, 0, 100.0f);
		glVertex3f(100.0f, 0, 100.0f);
	glEnd();

    srand(5);
	for(int i = 0; i < 200;) {
        float x = (rand() * 1.0f / RAND_MAX) * 200 - 100;
        float z = (rand() * 1.0f / RAND_MAX) * 200 - 100;
        float k = x * x + z * z;
        if (k > 2500){
            drawTree(x, z);
            i++;
        }
    }
	int n = 10;
	float div = ((2*M_PI) / n);
	glColor3f(0,0,1);

	glPushMatrix();
	glRotatef(rotAng,0,1,0);
	for(int i = 0; i < 10; i++){
	    float ang = i * 1.0f * div;
	    glPushMatrix();
	    glTranslatef(sin(ang)*15,1,cos(ang)*15);
	    glRotatef(90 + (ang*(180/M_PI)),0,1,0);
	    glutSolidTeapot(2);
	    glPopMatrix();
	}

	glRotatef(-2*rotAng,0,1,0);
	glColor3f(1,0,0);
    for(int i = 0; i < 10; i++){
        float ang = i * 1.0f * div;
        glPushMatrix();
        glTranslatef(sin(ang)*35,1,cos(ang)*35);
        glRotatef(90 + (ang*(180/M_PI)),0,1,0);
        glutSolidTeapot(2);
        glPopMatrix();
    }



    glPopMatrix();
    glColor3f(1,0,0);
	glutSolidTorus(1,2,6,10);

    rotAng += 0.4;
	// End of frame
	glutSwapBuffers();
}


void processKeys(unsigned char c, int xx, int yy) {

// put code to process regular keys in here

}


void processSpecialKeys(int key, int xx, int yy) {

	switch (key) {

	case GLUT_KEY_RIGHT:
		alfa -= 0.1; break;

	case GLUT_KEY_LEFT:
		alfa += 0.1; break;

	case GLUT_KEY_UP:
		beta += 0.1f;
		if (beta > 1.5f)
			beta = 1.5f;
		break;

	case GLUT_KEY_DOWN:
		beta -= 0.1f;
		if (beta < -1.5f)
			beta = -1.5f;
		break;

	case GLUT_KEY_PAGE_DOWN: radius -= 1.0f;
		if (radius < 1.0f)
			radius = 1.0f;
		break;

	case GLUT_KEY_PAGE_UP: radius += 1.0f; break;
	}
	spherical2Cartesian();
	glutPostRedisplay();

}


void printInfo() {

	printf("Vendor: %s\n", glGetString(GL_VENDOR));
	printf("Renderer: %s\n", glGetString(GL_RENDERER));
	printf("Version: %s\n", glGetString(GL_VERSION));

	printf("\nUse Arrows to move the camera up/down and left/right\n");
	printf("Home and End control the distance from the camera to the origin");
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
	glutIdleFunc(renderScene);
	
// Callback registration for keyboard processing
	glutKeyboardFunc(processKeys);
	glutSpecialFunc(processSpecialKeys);

//  OpenGL settings
	glEnable(GL_DEPTH_TEST);
	//glEnable(GL_CULL_FACE);

	spherical2Cartesian();

	printInfo();



// enter GLUT's main cycle
	glutMainLoop();
	
	return 1;
}
