#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glut.h>
#endif

#define _USE_MATH_DEFINES
#include <math.h>
/*
 * Metodo 1
float c_alpha = 0;
float c_beta = 0;
*/

float c_alpha = -(M_PI/2);
float c_beta = 0;
float vel = 0;

float px = 0;
float py = 0;
float pz = 5;



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


void drawCylinder(float radius, float height, int slices) {
    // put code to draw cylinder in here
    float div = (2*M_PI) / slices;
    glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
    glBegin(GL_TRIANGLES);
    for(float alpha = 0; alpha < 2*M_PI; alpha += div){
        glVertex3f(0,height/2,0);
        glVertex3f(cos(alpha + div)*(radius),height/2,sin(alpha + div)*(radius));
        glVertex3f(cos(alpha)*radius, height/2,sin(alpha)*radius);

        glVertex3f(0,-height/2,0);
        glVertex3f(cos(alpha)*radius, -height/2,sin(alpha)*radius);
        glVertex3f(cos(alpha + div)*(radius),-height/2,sin(alpha + div)*(radius));

        glVertex3f(cos(alpha)*radius, height/2,sin(alpha)*radius);
        glVertex3f(cos(alpha + div)*(radius),height/2,sin(alpha + div)*(radius));
        glVertex3f(cos(alpha + div)*(radius),-height/2,sin(alpha + div)*(radius));

        glVertex3f(cos(alpha)*radius, height/2,sin(alpha)*radius);
        glVertex3f(cos(alpha + div)*(radius),-height/2,sin(alpha + div)*(radius));
        glVertex3f(cos(alpha)*radius, -height/2,sin(alpha)*radius);
    }
    glEnd();
}


void renderScene(void) {

	// clear buffers
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// set the camera
	glLoadIdentity();

	/*
	float x = 7*cos(c_beta)*cos(c_alpha);
	float y = 7*sin(c_beta);
	float z = 7*cos(c_beta)*sin(c_alpha);

	gluLookAt(x,y,z,
		      0.0,0.0,0.0,
			  0.0f,1.0f,0.0f);
    */
	float dx = cos(c_beta)*cos(c_alpha);
	float dy = sin(c_beta);
	float dz = cos(c_beta)*sin(c_alpha);

	px += vel*dx;
	py += vel*dy;
    pz += vel*dz;

	//TODO movimentar o ponto da camara

	gluLookAt(px,py,pz,
	          px+dx,py+dy,pz+dz,
	          0.0f,0.1f,0.0f); //eixo do z é onde está a camara
	drawCylinder(1,2,10);
	vel = 0;

	// End of frame
	glutSwapBuffers();
}


void processKeys(unsigned char c, int xx, int yy) {

// put code to process regular keys in here
    if(c == 'w'){
        vel = 0.2;
        glutPostRedisplay();
    }
    if(c == 's'){
        vel = -0.2;
        glutPostRedisplay();
    }
    if(c == 'd'){
        c_alpha += (M_PI/20);
        glutPostRedisplay();
    }
    if(c == 'a'){
        c_alpha -= (M_PI/20);
        glutPostRedisplay();
    }

}


void processSpecialKeys(int key, int xx, int yy) {

// put code to process special keys in here
    if(key == GLUT_KEY_RIGHT){
        c_alpha += M_PI/3;
        glutPostRedisplay();
    }
    if(key == GLUT_KEY_LEFT){
        c_alpha -= M_PI/3;
        glutPostRedisplay();
    }
    if(key == GLUT_KEY_UP){
        c_beta += M_PI/3;
        glutPostRedisplay();
    }
    if(key == GLUT_KEY_DOWN){
        c_beta -= M_PI/3;
        glutPostRedisplay();
    }
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
	
// Callback registration for keyboard processing
	glutKeyboardFunc(processKeys);
	glutSpecialFunc(processSpecialKeys);

//  OpenGL settings
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);
	
// enter GLUT's main cycle
	glutMainLoop();
	
	return 1;
}
