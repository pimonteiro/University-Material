library(MASS)
library(corrplot)
library(pscl)
library(lmtest)
library(pscl)
library(leaps)
library(boot)
library(caret)


scale.many <- function(dat, column.nos) {
  nms <- names(dat)
  for(col in column.nos) {
    name <- paste(nms[col],".z", sep = "")
    dat[name] <- scale(dat[,col])
  }
  cat(paste("Scaled ", length(column.nos), " variable(s)\n"))
  dat
}


data <- read.csv("c:/Users/Filipe Monteiro/Downloads/data.csv")
summary(data)

data$X<-NULL
data$id <- NULL

new_values <- as.integer(data$diagnosis == 'B')
data$diagnosis <- new_values
head(data)


data.alt1 <- scale.many(data, c(2:11))
data.alt1[2:31] <- NULL
data.alt2 <- scale.many(data, c(12:21))
data.alt2[2:31] <- NULL
data.alt3 <- scale.many(data, c(22:31))
data.alt3[2:31] <- NULL

# First batch: Mean
names.alt <- names(data.alt1)
par(mfrow=c(2,2))	
boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 2]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 2]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[2])))
mylevels <- levels(data.alt1$diagnosis)
levelProportions <- summary(data.alt1$diagnosis)/nrow(data.alt1)
for(i in 1:length(mylevels)){
  thislevel <- mylevels[i]
  thisvalues <- data[data.alt1$diagnosis==thislevel, 2]
   
  # take the x-axis indices and add a jitter, proportional to the N in each level
  myjitter <- jitter(rep(i, length(thisvalues)), amount=levelProportions[i]/2)
  points(myjitter, thisvalues, pch=20, col=rgb(0,0,0,.9))   
}


boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 3]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 3]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[3])))
boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 4]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 4]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[4])))
boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 5]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 5]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[5])))

boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 6]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 6]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[6])))
boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 7]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 7]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[7])))
boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 8]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 8]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[8])))
boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 9]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 9]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[9])))

par(mfrow=c(1,2))
boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 10]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 10]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[10])))
boxplot(as.numeric(data.alt1[ which(data.alt1$diagnosis == 1), 11]), as.numeric(data.alt1[ which(data.alt1$diagnosis == 0), 11]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[11])))


# Second batch: SE
names.alt <- names(data.alt2)
par(mfrow=c(2,2))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 2]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 2]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[2])))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 3]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 3]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[3])))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 4]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 4]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[4])))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 5]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 5]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[5])))

boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 6]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 6]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[6])))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 7]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 7]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[7])))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 8]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 8]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[8])))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 9]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 9]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[9])))

par(mfrow=c(1,2))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 10]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 10]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[10])))
boxplot(as.numeric(data.alt2[ which(data.alt2$diagnosis == 1), 11]), as.numeric(data.alt2[ which(data.alt2$diagnosis == 0), 11]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[11])))


# Third batch: Worst
names.alt <- names(data.alt3)
par(mfrow=c(2,2))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 2]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 2]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[2])))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 3]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 3]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[3])))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 4]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 4]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[4])))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 5]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 5]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[5])))

boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 6]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 6]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[6])))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 7]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 7]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[7])))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 8]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 8]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[8])))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 9]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 9]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[9])))

par(mfrow=c(1,2))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 10]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 10]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[10])))
boxplot(as.numeric(data.alt3[ which(data.alt3$diagnosis == 1), 11]), as.numeric(data.alt3[ which(data.alt3$diagnosis == 0), 11]), names=c(1,0), main=paste(c("Diagnosis por", names.alt[11])))

# Correlation plot
corrplot(cor(data))

# Simple plot for showing correlation
plot(data$area_mean,data$perimeter_mean)
plot(data$area_mean,data$radius_mean)


# Regressão inicial
model.1 <- glm(diagnosis ~., data, family=binomial)


model.remove_cor <- c('radius_mean', 'area_mean', 'concavity_mean', 'compactness_mean', 'radius_se', 'perimeter_se', 'compactness_se', 'concavity_se', 'perimeter_worst', 'area_worst', 'texture_worst')

newdata <- data
newdata[ ,model.remove_cor] <- list(NULL)

model.2 <- glm(diagnosis ~., newdata, family=binomial)
pR2(model.2)
lrtest(model.1,model.2)

y_predicted <- predict(model.2, newx = newdata, type="response")
y_predicted <- ifelse(y_predicted > 0.5, 1, 0)
mean(y_predicted == newdata$diagnosis)


# Lasso Regression
library(glmnet)

X <- as.matrix(newdata[,-1])
Y <- newdata$diagnosis
model.lasso_cv <- cv.glmnet(X,Y,family = "binomial",alpha=1,type.measure = "class",nfolds = 10)
lambda.opt <- model.lasso_cv$lambda.min

model.lasso <- glmnet(X,Y,family="binomial", alpha=1, lambda=lambda.opt)
y_predicted <- predict(model.lasso, newx = X, type="response")
y_predicted <- ifelse(y_predicted > 0.5, 1, 0)
mean(y_predicted == newdata$diagnosis)
plot(model.lasso_cv)
coef(model.lasso)

model.3 <- glm(diagnosis~ . -perimeter_mean-smoothness_mean-symmetry_mean-fractal_dimension_mean-texture_se-smoothness_se-concave.points_se-symmetry_se-compactness_worst-fractal_dimension_worst, data=newdata, family=binomial)
y_predicted <- predict(model.2, newx = newdata, type="response")
y_predicted <- ifelse(y_predicted > 0.5, 1, 0)
mean(y_predicted == newdata$diagnosis)

# Ridge Regression

model.ridge_cv <- cv.glmnet(X,Y,family = "binomial",alpha=0,type.measure = "class",nfolds = 10)
lambda.opt <- model.ridge_cv$lambda.min

model.ridge <- glmnet(X,Y,family="binomial", alpha=0, lambda=lambda.opt)
y_predicted <- predict(model.ridge, newx = X, type="response")
y_predicted <- ifelse(y_predicted > 0.5, 1, 0)
mean(y_predicted == newdata$diagnosis)
plot(model.ridge_cv)
coef(model.ridge)


# Stepwise Regression
model.4 <- stepAIC(model.2, trace=FALSE, direction="backward")


# PCA 
cat("Arranque do PCA\n")

diagnosis = data[,1]
data.pca = data[,-1]

head(data.pca)
corr = cor(data.pca)
corrplot(corr,type="lower",title = "correlation of variable",tl.col=1,tl.cex=0.7)

a = 0.958042
pca = princomp(data.pca,cor=T)

summary(pca)
gof = (pca$sdev)^2/sum((pca$sdev)^2)

par(mfrow=c(3,1))
plot(gof, xlab = "Principal Componente", 
     ylab = "Proporçoã de Variação Explacativa", 
     ylim = c(0, 1), type = "b",main="Grafico 1")

plot(cumsum(gof), xlab = "Principal Componente", 
     ylab = "Proporção acumulada de variação explicada", 
     ylim = c(0, 1), type = "b", col="red", main="Grafico 2")
plot(pca, main = "Gráfico 3")

cex.before <- par("cex")
par(cex = 1)
biplot(pca)


newdata.pca = pca$scores[,1:6]

newdata.pca.set = cbind(diagnosis,newdata.pca)
colnames(newdata.pca.set) = c("diagnosis","P1","P2","P3","P4","P5","P6")
newdata.pca.set=as.data.frame(newdata.pca.set)
head(newdata.pca.set)

## 75% of the sample size
smp_size <- floor(0.75 * nrow(newdata.pca.set))

## set the seed to make your partition reproducible
set.seed(123)
train_ind <- sample(seq_len(nrow(newdata.pca.set)), size = smp_size)
train <- newdata.pca.set[train_ind, ]
test <- newdata.pca.set[-train_ind, ]
nrow(train)
nrow(test)
typeof(train)
train <- as.data.frame(train)
test <- as.data.frame(test)
dim(train)
model.2 <- glm(diagnosis~.,family = "binomial",data = train)
summary(model.2) 
head(test[,2:7])
data.pca.predict<-predict(model.2,newdata = test[,2:7],terms = "response")
data.pca.predict
data.pca.predict <- ifelse(data.pca.predict > 0.5, 1, 0)
mean(data.pca.predict == test$diagnosis)
plot(model.2)
coef(model.2)




## Analise de Resultados

model.2.cv <- cv.kcross(newdata,10)
dropList <- c("perimeter_mean","smoothness_mean","symmetry_mean","fractal_dimension_mean","texture_se","smoothness_se","concave.points_se","symmetry_se","compactness_worst","fractal_dimension_worst")
newdata.3 <- newdata[, !colnames(newdata) %in% dropList]
model.3.cv <- cv.kcross(newdata.3,10)
dropList <- c("texture_mean","perimeter_mean","concave.points_mean","fractal_dimension_mean","area_se","smoothness_se","symmetry_se","fractal_dimension_se","radius_worst","compactness_worst","concavity_worst","symmetry_worst","fractal_dimension_worst")
newdata.4 <- newdata[, !colnames(newdata) %in% dropList]
model.4.cv <- cv.kcross(newdata.4,10)
model.5.cv <- cv.kcross(newdata.pca.set,10)

cv.kcross <- function(data, k) {
	set.seed(1)
	accuracy <- NULL
	fpr <- NULL
	fnr <- NULL
	aic <- NULL
	lst_mdl <- NULL
	for(i in 1:k){
	    	smp_size <- floor(0.95 * nrow(data))
    		index <- sample(seq_len(nrow(data)),size=smp_size)
    		train <- data[index, ]
    		test <- data[-index, ]
		
		model <- glm(diagnosis ~ ., data=train, family=binomial)
		lst_mdl <- model
		results <- predict(model, newdata=test, type='response')
		results <- ifelse(results > 0.5,1,0)

		misClasificError <- mean(test$diagnosis == results)
		accuracy[i] <- misClasificError
		aic[i] <- model$aic
		cm <- confusionMatrix(data=as.factor(results), reference=as.factor(test$diagnosis))
		fpr[i] <- cm$table[2]/(nrow(data)-smp_size)
	    	fnr[i] <- cm$table[3]/(nrow(data)-smp_size)
	}
	
	mean_acc <- mean(accuracy)
	mean_fpr <- mean(fpr)
	mean_fnr <- mean(fnr)
	mean_aic <- mean(aic)
	ret <- list(mean_acc=mean_acc, mean_aic=mean_aic, mean_fpr=mean_fpr, mean_fnr=mean_fnr, last_model=lst_mdl)
	return(ret)
} 





