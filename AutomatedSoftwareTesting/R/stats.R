# Obs: It works only if the script is "Sourced" before the run.
# This can be done via the command: source('path/to/this/file/plots2.R')
# The suggestion is to use Rstudio as IDE and make sure to have the flag "Source on Save" checked.
# ( see: http://stackoverflow.com/questions/13672720/r-command-for-setting-working-directory-to-source-file-location )
this.dir <- dirname(parent.frame(2)$ofile)
setwd(this.dir)

library(ggplot2)
options(gsubfn.engine = "R") # Thanks to http://stackoverflow.com/questions/17128260/r-stuck-in-loading-sqldf-package
library(sqldf)

dat <- read.csv("../stats.csv")
dat2 <- read.csv("../stats2.csv")

grafici <- function() {
  #p1 <- ggplot(dat, aes(method,successful, fill=method)) + geom_boxplot(varwidth=T, outlier.shape=NA) + #avoid plotting outliers twice
    geom_jitter(position=position_jitter(width=.1, height=0))
    
    p1 <- ggplot(dat, aes(method,successful, fill=method)) + geom_violin() + #avoid plotting outliers twice
      geom_jitter(position=position_jitter(width=.1, height=0))
  ggsave(p1,file="plotSuccessRate.png", width=6, height=5)
  
  p2 <- ggplot(dat, aes(method,evaluationsConsumed, fill=method)) + geom_violin() + #avoid plotting outliers twice
    geom_jitter(position=position_jitter(width=.1, height=0))
  ggsave(p2,file="plotEvaluationTime.png", width=6, height=5)
  
  #wilcox.test(evaluationsConsumed ~ method, data=dat) 
  
  #fisher.test(successful ~ method, data=dat)
  results <- sqldf("select method, cast(sum(successful) as float)/cast(count(*) as float) as success, 1.0-cast(sum(successful) as float)/cast(count(*) as float) as notSuccess, avg(evaluationsConsumed) as avgFitnessEvaluation from dat group by method")
  #m <- sqldf("select sum(successful) as ns, 1000-sum(successful) as s from dat group by method")
  #fisher.test(m, alternative="two.sided")
  
  p3 <- ggplot(sqldf("select method,iteration,avg(fitness) as bestFitness from dat2 group by method,iteration"), aes(iteration, bestFitness, color=method)) + geom_line()
  ggsave(p3,file="plotFitness.png", width=6, height=5)
  
  dat2$success <- ifelse(dat2$fitness==0 , 1, 0)
  p4 <- ggplot(sqldf("select method,iteration,sum(success)/1000 as probSuccess from dat2 group by method,iteration"), aes(iteration, probSuccess, color=method)) + geom_line() + scale_y_continuous(limits = c(0, 1))
  ggsave(p4,file="plotFitness2.png", width=6, height=5)
}

#grafici()

library(doBy)
# see http://stackoverflow.com/questions/25198442/how-to-calculate-mean-median-per-group-in-a-dataframe-in-r
summaryBy(evaluationsConsumed ~ method, data = dat, FUN = list(mean, max, min, median, sd))