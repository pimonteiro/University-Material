#include <stdlib.h>
#include <stdio.h>

#ifdef __APPLE__
#include <GLUT/glut.h>
#else
#include <GL/glew.h>
#include <GL/glut.h>
#endif

#include <math.h>

#define _PI_ 3.14159

float alfa = 0.0f, beta = 0.0f, radius = 5.0f;
float camX, camY, camZ;

GLuint vertexCount, vertices[2], normalCount;

int timebase = 0, frame = 0;

void cross(float *a, float *b, float *res) {
    res[0] = a[1] * b[2] - a[2] * b[1];
    res[1] = a[2] * b[0] - a[0] * b[2];
    res[2] = a[0] * b[1] - a[1] * b[0];
}


void normalize(float *a) {
    float l {static_cast<float>(sqrt(a[0] * a[0] + a[1] * a[1] + a[2] * a[2]))};
    a[0] = a[0] / l;
    a[1] = a[1] / l;
    a[2] = a[2] / l;
}


void sphericalToCartesian() {

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

	// Reset the coordinate system before modifying
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	
	// Set the viewport to be the entire window
    glViewport(0, 0, w, h);

	// Set the correct perspective
	gluPerspective(45,ratio,1,1000);

	// return to the model view matrix mode
	glMatrixMode(GL_MODELVIEW);
}



void prepareCilinder(float height, float radius, int sides) {

	float *v;

	v = (float *)malloc(sizeof(float) * 4 * 3 * 3 * sides);

	float *normal;

	normal = (float *)malloc(sizeof(float) * 4 * 3 * 3 * sides);

	int vertex = 0;
	int norm = 0;
	float delta = 2.0f * _PI_ / sides;

	for (int i = 0; i < sides; ++i) {
		// top
		// central point
		v[vertex*3 + 0] = 0.0f; 
		v[vertex*3 + 1] = height /2.0f;
		v[vertex*3 + 2] = 0.0f;
        normal[norm*3 + 0] = 0.0f;
        normal[norm*3 + 1] = 1.0f;
        normal[norm*3 + 2] = 0.0f;

		vertex++;
		norm++;
		v[vertex*3 + 0] = radius * sin( i * delta);
		v[vertex*3 + 1] = height /2.0f;
		v[vertex*3 + 2] = radius * cos( i * delta);
        normal[norm*3 + 0] = 0.0f;
        normal[norm*3 + 1] = 1.0f;
        normal[norm*3 + 2] = 0.0f;

		vertex++;
		norm++;
		v[vertex*3 + 0] = radius * sin( (i+1) * delta);
		v[vertex*3 + 1] = height /2.0f;
		v[vertex*3 + 2] = radius * cos( (i+1) * delta);
		normal[norm*3 + 0] = 0.0f;
        normal[norm*3 + 1] = 1.0f;
        normal[norm*3 + 2] = 0.0f;

		// body
		// tri�ngulo 1
		vertex++;
		norm++;
		v[vertex*3 + 0] = radius * sin( (i+1) * delta);
		v[vertex*3 + 1] = height /2.0f;
		v[vertex*3 + 2] = radius * cos( (i+1) * delta);

		vertex++;
		v[vertex*3 + 0] = radius * sin( i * delta);
		v[vertex*3 + 1] = height /2.0f;
		v[vertex*3 + 2] = radius * cos( i * delta);

		vertex++;
		v[vertex*3 + 0] = radius * sin( i * delta);
		v[vertex*3 + 1] = -height /2.0f;
		v[vertex*3 + 2] = radius * cos( i * delta);

        float vect1[3] {
                v[(vertex-1)*3 + 0]- v[(vertex-2)*3 + 0],
                v[(vertex-1)*3 + 2]- v[(vertex-2)*3 + 2],
                v[(vertex-1)*3 + 3]- v[(vertex-2)*3 + 3]
        };
        float vect2[3] {
                v[(vertex)*3 + 0]- v[(vertex-2)*3 + 0],
                v[(vertex)*3 + 2]- v[(vertex-2)*3 + 2],
                v[(vertex)*3 + 3]- v[(vertex-2)*3 + 3]
        };
        float crossed[3];
        cross(vect1,vect2,crossed);
        normalize(crossed);
        normal[norm*3 + 0] = crossed[0];
        normal[norm*3 + 1] = crossed[1];
        normal[norm*3 + 2] = crossed[2];
        norm++;

        vect1[0] = v[(vertex-2)*3 + 0]- v[(vertex-1)*3 + 0];
        vect1[1] = v[(vertex-2)*3 + 0]- v[(vertex-1)*3 + 0];
        vect1[2] = v[(vertex-2)*3 + 0]- v[(vertex-1)*3 + 0];
        vect2[0] = v[(vertex)*3 + 0]- v[(vertex-1)*3 + 0];
        vect2[1] = v[(vertex)*3 + 0]- v[(vertex-1)*3 + 0];
        vect2[2] = v[(vertex)*3 + 0]- v[(vertex-1)*3 + 0];
        cross(vect1,vect2,crossed);
        normalize(crossed);
        normal[norm*3 + 0] = crossed[0];
        normal[norm*3 + 1] = crossed[1];
        normal[norm*3 + 2] = crossed[2];
        norm++;

        vect1[0] = v[(vertex-2)*3 + 0]- v[(vertex)*3 + 0];
        vect1[1] = v[(vertex-2)*3 + 0]- v[(vertex)*3 + 0];
        vect1[2] = v[(vertex-2)*3 + 0]- v[(vertex)*3 + 0];
        vect2[0] = v[(vertex-1)*3 + 0]- v[(vertex)*3 + 0];
        vect2[1] = v[(vertex-1)*3 + 0]- v[(vertex)*3 + 0];
        vect2[2] = v[(vertex-1)*3 + 0]- v[(vertex)*3 + 0];
        cross(vect1,vect2,crossed);
        normalize(crossed);
        normal[norm*3 + 0] = crossed[0];
        normal[norm*3 + 1] = crossed[1];
        normal[norm*3 + 2] = crossed[2];
        norm++;

		// triangle 2
		vertex++;
		v[vertex*3 + 0] = radius * sin( (i+1) * delta);
		v[vertex*3 + 1] = -height /2.0f;
		v[vertex*3 + 2] = radius * cos( (i+1) * delta);

		vertex++;
		v[vertex*3 + 0] = radius * sin( (i+1) * delta);
		v[vertex*3 + 1] = height /2.0f;
		v[vertex*3 + 2] = radius * cos( (i+1) * delta);

		vertex++;
		v[vertex*3 + 0] = radius * sin( i * delta);
		v[vertex*3 + 1] = -height /2.0f;
		v[vertex*3 + 2] = radius * cos( i * delta);

        float vect3[3] {
                v[(vertex-1)*3 + 0]- v[(vertex-2)*3 + 0],
                v[(vertex-1)*3 + 2]- v[(vertex-2)*3 + 2],
                v[(vertex-1)*3 + 3]- v[(vertex-2)*3 + 3]
        };
        float vect4[3] {
                v[(vertex)*3 + 0]- v[(vertex-2)*3 + 0],
                v[(vertex)*3 + 2]- v[(vertex-2)*3 + 2],
                v[(vertex)*3 + 3]- v[(vertex-2)*3 + 3]
        };
        float crossed2[3];
        cross(vect3,vect4,crossed2);
        normalize(crossed);
        normal[norm*3 + 0] = crossed2[0];
        normal[norm*3 + 1] = crossed2[1];
        normal[norm*3 + 2] = crossed2[2];
        norm++;

        vect3[0] = v[(vertex-2)*3 + 0]- v[(vertex-1)*3 + 0];
        vect3[1] = v[(vertex-2)*3 + 0]- v[(vertex-1)*3 + 0];
        vect3[2] = v[(vertex-2)*3 + 0]- v[(vertex-1)*3 + 0];
        vect4[0] = v[(vertex)*3 + 0]- v[(vertex-1)*3 + 0];
        vect4[1] = v[(vertex)*3 + 0]- v[(vertex-1)*3 + 0];
        vect4[2] = v[(vertex)*3 + 0]- v[(vertex-1)*3 + 0];
        cross(vect3,vect4,crossed2);
        normalize(crossed);
        normal[norm*3 + 0] = crossed2[0];
        normal[norm*3 + 1] = crossed2[1];
        normal[norm*3 + 2] = crossed2[2];
        norm++;

        vect3[0] = v[(vertex-2)*3 + 0]- v[(vertex)*3 + 0];
        vect3[1] = v[(vertex-2)*3 + 0]- v[(vertex)*3 + 0];
        vect3[2] = v[(vertex-2)*3 + 0]- v[(vertex)*3 + 0];
        vect4[0] = v[(vertex-1)*3 + 0]- v[(vertex)*3 + 0];
        vect4[1] = v[(vertex-1)*3 + 0]- v[(vertex)*3 + 0];
        vect4[2] = v[(vertex-1)*3 + 0]- v[(vertex)*3 + 0];
        cross(vect3,vect4,crossed2);
        normalize(crossed2);
        normal[norm*3 + 0] = crossed2[0];
        normal[norm*3 + 1] = crossed2[1];
        normal[norm*3 + 2] = crossed2[2];
        norm++;


        // base
		// central point
		vertex++;
		v[vertex*3 + 0] = 0.0f; 
		v[vertex*3 + 1] = -height /2.0f;
		v[vertex*3 + 2] = 0.0f;
        normal[norm*3 + 0] = 0.0f;
        normal[norm*3 + 1] = -1.0f;
        normal[norm*3 + 2] = 0.0f;

		vertex++;
		norm++;
		v[vertex*3 + 0] = radius * sin( (i+1) * delta);
		v[vertex*3 + 1] = -height /2.0f;
		v[vertex*3 + 2] = radius * cos( (i+1) * delta);
        normal[norm*3 + 0] = 0.0f;
        normal[norm*3 + 1] = -1.0f;
        normal[norm*3 + 2] = 0.0f;

		vertex++;
		norm++;
		v[vertex*3 + 0] = radius * sin( i * delta);
		v[vertex*3 + 1] = -height /2.0f;
		v[vertex*3 + 2] = radius * cos( i * delta);
        normal[norm*3 + 0] = 0.0f;
        normal[norm*3 + 1] = -1.0f;
        normal[norm*3 + 2] = 0.0f;

		vertex++;
		norm++;
	}

	vertexCount = vertex;
	normalCount = norm;

	glGenBuffers(2, vertices);
	glBindBuffer(GL_ARRAY_BUFFER,vertices[0]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(float) * vertexCount * 3, v,     GL_STATIC_DRAW);

	glBindBuffer(GL_ARRAY_BUFFER,vertices[1]);
	glBufferData(GL_ARRAY_BUFFER, sizeof(float) * normalCount * 3, normal, GL_STATIC_DRAW);

	free(v);
    free(normal);
}


void drawCilinder() {
		
	glBindBuffer(GL_ARRAY_BUFFER,vertices[0]);
	glVertexPointer(3,GL_FLOAT,0,0);
	glDrawArrays(GL_TRIANGLES, 0, vertexCount);

	//glBindBuffer(GL_ARRAY_BUFFER, vertices[1]);
	//glNormalPointer(GL_FLOAT,0,0);
	//glDrawArrays(GL_TRIANGLES, 0, normalCount);
}


void renderScene(void) {

	float pos[4] = {1.0, 1.0, 1.0, 0.0};
	float fps;
	int time;
	char s[64];

	glClearColor(0.0f,0.0f,0.0f,0.0f);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	glLoadIdentity();

	gluLookAt(camX,camY,camZ, 
		      0.0,0.0,0.0,
			  0.0f,1.0f,0.0f);

	drawCilinder();

	frame++;
	time=glutGet(GLUT_ELAPSED_TIME); 
	if (time - timebase > 1000) { 
		fps = frame*1000.0/(time-timebase); 
		timebase = time; 
		frame = 0; 
		sprintf(s, "FPS: %f6.2", fps);
		glutSetWindowTitle(s);
	} 

// End of frame
	glutSwapBuffers();
}



void processKeys(int key, int xx, int yy) 
{
	switch(key) {
	
		case GLUT_KEY_RIGHT: 
						alfa -=0.1; break;

		case GLUT_KEY_LEFT: 
						alfa += 0.1; break;

		case GLUT_KEY_UP : 
						beta += 0.1f;
						if (beta > 1.5f)
							beta = 1.5f;
						break;

		case GLUT_KEY_DOWN: 
						beta -= 0.1f; 
						if (beta < -1.5f)
							beta = -1.5f;
						break;

		case GLUT_KEY_PAGE_DOWN : radius -= 0.1f; 
			if (radius < 0.1f)
				radius = 0.1f;
			break;

		case GLUT_KEY_PAGE_UP: radius += 0.1f; break;

	}
	sphericalToCartesian();

}



void initGL() {

// OpenGL settings 
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);

// init
	sphericalToCartesian();
	glEnableClientState(GL_VERTEX_ARRAY);
    glEnableClientState(GL_NORMAL_ARRAY);

	prepareCilinder(2,1,16);
}


int main(int argc, char **argv) {

// init
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_DEPTH|GLUT_DOUBLE|GLUT_RGBA);
	glutInitWindowPosition(100,100);
	glutInitWindowSize(320,320);
	glutCreateWindow("CG@DI-UM");
		
// callback registration
	glutDisplayFunc(renderScene);
	glutIdleFunc(renderScene);
	glutReshapeFunc(changeSize);

// keyboard callback registration
	glutSpecialFunc(processKeys);

#ifndef __APPLE__	
// init GLEW
	glewInit();
#endif

	initGL();

	glutMainLoop();
	
	return 1;
}

